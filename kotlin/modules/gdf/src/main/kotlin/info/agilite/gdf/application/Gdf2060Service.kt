package info.agilite.gdf.application

import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.security.UserContext
import info.agilite.cas.adapter.infra.Cas65Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.xml.ElementXmlConverter
import info.agilite.gdf.adapter.infra.Gdf2060Repository
import info.agilite.gdf.adapter.infra.Gdf20Repository
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.shared.events.Srf01FiscalAprovadoEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class Gdf2060Service(
  private val cas65repo: Cas65Repository,
  private val gdf2060repo: Gdf2060Repository,
  private val gdf20repo: Gdf20Repository,
  private val eventPublish: ApplicationEventPublisher,
) {

  fun processarRetornoLoteNFse(gdf20id: Long, bytes: ByteArray, contentType: String?){
    val cas65 = cas65repo.findById(UserContext.safeUser.empId)
    if(cas65.cas65municipio == null || cas65.cas65uf == null){
      throw RuntimeException("Município ou UF não configurados para a empresa logada")
    }
    val municipioEstado = "${cas65.cas65municipio}-${cas65.cas65uf}".uppercase()
    val dadosRps: List<DadosRps>
    if(municipioEstado == "ITATIBA-SP") {
      dadosRps = extrairDadosRetornoItatiba(bytes, contentType)
    }else{
      throw RuntimeException("Não é possível processar retorno de lotes de NFSe para o município de ${cas65.cas65municipio}-${cas65.cas65uf}")
    }

    val srf01ids = processarDadosDoRetornoDasNFSe(dadosRps, gdf20id)
    if(srf01ids.isNotEmpty()){
      eventPublish.publishEvent(Srf01FiscalAprovadoEvent(this, srf01ids))
    }
  }

  private fun processarDadosDoRetornoDasNFSe(dadosRps: List<DadosRps>, gdf20id: Long): List<Long> {
    if (dadosRps.isEmpty()) throw ValidationException("Nenhum RPS encontrado no retorno")
    val srf01processarRetorno = gdf2060repo.findNFSeFromLoteToProcessar(gdf20id)
    val srf01ids = mutableListOf<Long>()

    val batch = BatchOperations()
    for (index in dadosRps.indices) {
      val rps = dadosRps[index]
      val dadosSrf01Processar = srf01processarRetorno.find { srf01 ->
        srf01.srf01serie == rps.serieRps &&
        srf01.srf01numero == rps.numeroRps
      }
      if (dadosSrf01Processar == null) {
        throw ValidationException("RPS ${rps.serieRps}-${rps.numeroRps} não encontrado no lote de processamento")
      }

      if (dadosSrf01Processar.srf01numero != rps.numeroNFSe) {
        dadosSrf01Processar.srf01numero = rps.numeroNFSe
        dadosSrf01Processar.srf01integracaoGdf = INTEGRACAO_OK
        srf01ids.add(dadosSrf01Processar.srf01id)
        batch.update(dadosSrf01Processar)
      }
    }
    gdf2060repo.executeBatch(batch)
    gdf20repo.delete(gdf20id)

    return srf01ids
  }

  fun extrairDadosRetornoItatiba(bytes: ByteArray, contentType: String?): List<DadosRps>{
    if(!contentType.equals("application/xml", true)){
      throw RuntimeException("O conteúdo do retorno deve ser um XML")
    }

    val xml = String(bytes)
    val xmlRoot = ElementXmlConverter.string2Element(xml)
    val nfses = xmlRoot.findChildNodes("Nfse") ?: return emptyList()

    val result = mutableListOf<DadosRps>()
    for(index in nfses.indices){
      val nfse = nfses[index]
      val numeroNFSe = nfse.findChildValue("InfNfse.Numero") ?: throw ValidationException("Número da NFSe não encontrado na nota $index")
      val serieRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Serie") ?: throw ValidationException("Série do RPS não encontrado na nota $index")
      val numeroRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Numero") ?: throw ValidationException("Número do RPS não encontrado na nota $index")

      result.add(DadosRps(numeroRps.toInt(), serieRps?.toInt(), numeroNFSe.toInt()))
    }

    return result
  }
}

data class DadosRps(
  val numeroRps: Int,
  val serieRps: Int?,
  val numeroNFSe: Int,
)

@DbTable("Srf01")
data class DadosSrf01ProcessarRetorno (
  val srf01id: Long,
  val srf01serie: Int?,
  var srf01numero: Int,
  var srf01integracaoGdf: Int,
)
