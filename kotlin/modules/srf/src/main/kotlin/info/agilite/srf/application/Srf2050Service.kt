package info.agilite.srf.application

import info.agilite.boot.security.UserContext
import info.agilite.core.certs.Certificado
import info.agilite.core.certs.CertificadoFactory
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.formatISO
import info.agilite.core.extensions.maxLenght
import info.agilite.core.extensions.numbersOnly
import info.agilite.core.extensions.roud
import info.agilite.core.xml.AssinadorXML
import info.agilite.core.xml.ElementXml
import info.agilite.shared.entities.cas.Cas65
import info.agilite.shared.entities.cgs.CGS80TIPO_PESSOA_FISICA
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.events.srf.Srf2050EventLoteGerado
import info.agilite.srf.adapter.infra.Srf01Repository
import info.agilite.srf.adapter.infra.Srf2050Repository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class SRF2050Service(
  private val srf2050repo: Srf2050Repository,
  private val srf01Repository: Srf01Repository,
  private val eventPublish: ApplicationEventPublisher,
) {
  fun emitirLoteXmlPrefeitura(srf01ids: List<Long>?, sseUid: String): String {
    val srf01idsEmitir = srf01ids ?: srf2050repo.findsNFSeToExport().map { it.srf01id }
    if(srf01idsEmitir.isNullOrEmpty()){
      throw ValidationException("Nenhuma nota fiscal encontrada para emitir lote")
    }

    val cas65 = srf2050repo.findById(Cas65::class, UserContext.safeUser.empId) ?: throw RuntimeException("Empresa logada não encontrada")
    if(cas65.cas65municipio == null || cas65.cas65uf == null){
      throw ValidationException("Município ou UF não configurados para a empresa logada")
    }
    val srf01s = srf01Repository.findWithEntidadeByIds(srf01idsEmitir)

    val municipioEstado = "${cas65.cas65municipio}-${cas65.cas65uf}".uppercase()
    val xml: String;
    if(municipioEstado == "ITATIBA-SP"){
       xml = GDF2020EmissorItatiba().emitirLoteXmlPrefeituraItatiba(cas65, srf01s, sseUid)
    }else{
      throw ValidationException("Não é possível emitir lotes de NFSe para o município de ${cas65.cas65municipio}-${cas65.cas65uf}")
    }
    eventPublish.publishEvent(Srf2050EventLoteGerado(this, xml, srf01idsEmitir))

    return xml
  }
}

class GDF2020EmissorItatiba() {
  private val ibgeItatiba = "3523404"

  fun emitirLoteXmlPrefeituraItatiba(cas65: Cas65, srf01s: List<Srf01>, sseUid: String): String {
    val enviarloteRpsEnvio = ElementXml.create("EnviarLoteRpsEnvio", "http://iss.itatiba.sp.gov.br/Arquivos/nfseV202.xsd")

    val loteRps = enviarloteRpsEnvio.addNode("LoteRps")
    loteRps.setAttribute("Id", "lote")
    loteRps.setAttribute("versao", "2.02")

    var numLote = System.currentTimeMillis()
    loteRps.addNode("NumeroLote", numLote)

    val cpfCnpj = loteRps.addNode("CpfCnpj")
    cpfCnpj.addNode("Cnpj", cas65.cas65cnpj?.numbersOnly(), true)

    loteRps.addNode("InscricaoMunicipal", cas65.cas65im?.numbersOnly(), true)
    loteRps.addNode("QuantidadeRps", srf01s.size, true)

    val listaRps = loteRps.addNode("ListaRps")
    srf01s.forEach { srf01 ->
      //super.setMensagem("Gerando xml da NFS-e: " + ea01.getEa01num()) TODO SSE
      val rps1 = listaRps.addNode("Rps")

      val infDeclaracaoPrestacaoServico = rps1.addNode("InfDeclaracaoPrestacaoServico")
      infDeclaracaoPrestacaoServico.setAttribute("Id", "Id"+ (numLote++))

      val rps2 = infDeclaracaoPrestacaoServico.addNode("Rps")

      val identificacaoRps = rps2.addNode("IdentificacaoRps")
      identificacaoRps.addNode("Numero", srf01.srf01numero, true)
      identificacaoRps.addNode("Serie", srf01.srf01serie ?: 0)
      identificacaoRps.addNode("Tipo", "1")

      val emissao = LocalDateTime.of(srf01.srf01dtEmiss ?: LocalDate.now(), LocalTime.now().minusHours(2))
      rps2.addNode("DataEmissao", emissao.formatISO())
      rps2.addNode("Status", 1)

      val listaServicos = infDeclaracaoPrestacaoServico.addNode("ListaServicos")
      srf01.srf011s!!.forEach { srf011 ->
        val servico = listaServicos.addNode("Servico")
        val valores = servico.addNode("Valores")

        valores.addNode("ValorServicos", srf011.srf011vlrTotal?.roud(2), true)
        valores.addNode("ValorDeducoes", "0.00") // TODO adicionar no SRF011
        valores.addNode("ValorIss", srf011.srf011vlrIss?.roud(2) ?: "0.00", true)
        valores.addNode("Aliquota", srf011.srf011alqIss?.roud(2) ?: "0.00", true)
        valores.addNode("BaseCalculo", srf011.srf011vlrTotal?.roud(2), true)
        servico.addNode("IssRetido", 2)//TODO adicionar tipo iss no PCD/Item

        val itemListaServico = srf011.srf011item?.cgs50tipoServico?.substringBefore('/')
        servico.addNode("ItemListaServico", itemListaServico, true)
        servico.addNode("CodigoCnae", cas65.cas65cnae?.numbersOnly(), false)

        servico.addNode("Discriminacao", srf011.srf011item?.cgs50descr, true)
        servico.addNode("CodigoMunicipio", ibgeItatiba, true) //TODO obter a partir do municipio do cliente
        servico.addNode("ExigibilidadeISS", 1) //TODO adicionar ao item da nota (Srf011)
        servico.addNode("MunicipioIncidencia", ibgeItatiba, true)

      }
      infDeclaracaoPrestacaoServico.addNode("Competencia",  emissao.formatISO())

      val prestador = infDeclaracaoPrestacaoServico.addNode("Prestador")
      val cpfCnpjPrestador = prestador.addNode("CpfCnpj")
      cpfCnpjPrestador.addNode("Cnpj", cas65.cas65cnpj?.numbersOnly(), true)
      prestador.addNode("InscricaoMunicipal", cas65.cas65im?.numbersOnly(), false)

      val tomadorServico = infDeclaracaoPrestacaoServico.addNode("TomadorServico")
      val identificacaoTomador = tomadorServico.addNode("IdentificacaoTomador")
      val cpfCnpjTomador = identificacaoTomador.addNode("CpfCnpj")
      cpfCnpjTomador.addNode(if(srf01.srf01entidade?.cgs80tipo == CGS80TIPO_PESSOA_FISICA) "Cpf" else "Cnpj", srf01.srf01entidade?.cgs80ni?.numbersOnly() , true)
      tomadorServico.addNode("RazaoSocial", srf01.srf01entidade?.cgs80nome, true)

      val entidade = srf01.srf01entidade
      val endereco = tomadorServico.addNode("Endereco")
      endereco.addNode("Endereco", entidade?.cgs80endereco, true)
      endereco.addNode("Numero", entidade?.cgs80numero, false)
      endereco.addNode("Complemento", entidade?.cgs80complemento, false)
      endereco.addNode("Bairro", entidade?.cgs80bairro, true)
      endereco.addNode("CodigoMunicipio", ibgeItatiba, true)//TODO obter a partir do municipio do cliente
      endereco.addNode("Uf", entidade?.cgs80uf, false)
      endereco.addNode("CodigoPais", null, false)
      endereco.addNode("Cep", entidade?.cgs80cep?.numbersOnly(), true)

      val contato = tomadorServico.addNode("Contato")
      contato.addNode("Telefone",  entidade?.cgs80telefone?.numbersOnly(), false)
      contato.addNode("Email", entidade?.cgs80email?.maxLenght(80), false)

      infDeclaracaoPrestacaoServico.addNode("OptanteSimplesNacional", 1) // TODO simples do cadastro de empresas (da empresa logada não do cliente)
      infDeclaracaoPrestacaoServico.addNode("OutrasInformacoes", null, false) // TODO OBS no SRF01 que venha do cadastro de empresa quando houver

      val valoresServico = infDeclaracaoPrestacaoServico.addNode("ValoresServico") // TODO adicionar todos os valores que estão com 0.00 ao SRf01
      valoresServico.addNode("ValorPis", "0.00")
      valoresServico.addNode("ValorCofins", "0.00")
      valoresServico.addNode("ValorInss", "0.00")
      valoresServico.addNode("ValorIr", "0.00")
      valoresServico.addNode("ValorCsll", "0.00")
      valoresServico.addNode("ValorIss", "0.00")
      valoresServico.addNode("ValorLiquidoNfse", srf01.srf01vlrTotal?.roud(2), true)
      valoresServico.addNode("ValorServicos", srf01.srf01vlrTotal?.roud(2), true)
    }

    //TODO obter certificado do repositório de certificados
    val certificado = CertificadoFactory.loadFromWindows("AGILITE SISTEMAS E INFORMATICA LTDA:33216155000119")
    val xml = assinarLoteNFSe(enviarloteRpsEnvio.toXmlStringTrim(), certificado)

    return xml
  }

  private fun assinarLoteNFSe(xml: String, certificado: Certificado): String {
    var xmlAssinado = AssinadorXML().assinar(xml, "InfDeclaracaoPrestacaoServico", certificado)
    xmlAssinado = AssinadorXML().assinar(xmlAssinado, "LoteRps", certificado)

    return xmlAssinado
  }
}