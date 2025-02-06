package info.agilite.srf.application

import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.orm.Where
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.where
import info.agilite.boot.security.UserContext
import info.agilite.boot.sse.SseEmitter
import info.agilite.cas.adapter.infra.Cas65Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.format
import info.agilite.core.extensions.orExc
import info.agilite.core.extensions.parseDateTime
import info.agilite.core.xml.ElementXmlConverter
import info.agilite.shared.RegOrigem
import info.agilite.shared.entities.gdf.GDF10SISTEMA_NFSE
import info.agilite.shared.entities.gdf.GDF10STATUSPROC_APROVADO
import info.agilite.shared.entities.gdf.GDF10TIPODOC_DOCUMENTO
import info.agilite.shared.entities.gdf.Gdf10
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.shared.events.Srf01FiscalProcessadoEvent
import info.agilite.srf.domain.Srf01Model
import info.agilite.srf.domain.Srf2051Model
import info.agilite.srf.infra.Srf2051Repository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@Service
class Srf2051Service(
  private val cas65repo: Cas65Repository,
  private val srf2051repo: Srf2051Repository,
  private val eventPublish: ApplicationEventPublisher,
  private val sseEmitter: SseEmitter
) {
  fun processarRetornoLoteNFse(xml: String): Srf2051Model {
    val cas65 = cas65repo.findById(UserContext.safeUser.empId)

    if (cas65.cas65municipio == null || cas65.cas65uf == null) {
      throw RuntimeException("Município ou UF não configurados para a empresa logada")
    }
    val municipioEstado = "${cas65.cas65municipio}-${cas65.cas65uf}".uppercase()
    val dadosRps: List<DadosRps>
    if (municipioEstado == "ITATIBA-SP") {
      dadosRps = extrairDadosRetornoItatiba(xml)
    } else {
      throw RuntimeException("Não é possível processar retorno de lotes de NFSe para o município de ${cas65.cas65municipio}-${cas65.cas65uf}")
    }

    val srf01s = processarDadosDoRetornoDasNFSe(dadosRps)
    return Srf2051Model(srf2051repo.findCountNFSeParaProcessar(), srf01s.map {
      Srf01Model(it.srf01numero, it.srf01dtEmiss, it.srf01nome, it.srf01vlrTotal)
    })
  }

  private fun processarDadosDoRetornoDasNFSe(dadosRpsPrefeitura: List<DadosRps>): List<Srf01> {
    if (dadosRpsPrefeitura.isEmpty()) throw ValidationException("Nenhum RPS encontrado no retorno")
    val srf01processarRetorno = srf2051repo.findNFSeParaProcessar()

    val batch = BatchOperations()
    val srf01s = mutableListOf<Srf01>();
    for (index in dadosRpsPrefeitura.indices) {
      sseEmitter.sendMessage("Processando NFSe ${index + 1} de ${dadosRpsPrefeitura.size}")
      val rps = dadosRpsPrefeitura[index]
      val srf01ByRPS = srf01processarRetorno.find { srf01 ->
        srf01.srf01serie == rps.serieRps &&
        srf01.srf01numero == rps.numeroRps
      }
      if (srf01ByRPS == null) {
        throw ValidationException("RPS ${rps.serieRps}-${rps.numeroRps} não encontrado no lote de processamento")
      }

      srf01ByRPS.srf01integracaoGdf = INTEGRACAO_OK
      srf01ByRPS.srf01numero = rps.numeroNFSe
      srf01ByRPS.srf01dfeAprov = createGdf10(rps.protocolo, srf01ByRPS, rps)
      srf01s.add(srf01ByRPS)

      eventPublish.publishEvent(Srf01FiscalProcessadoEvent(this, srf01ByRPS, rps.protocolo))
      batch.updateChanges(srf01ByRPS)
    }
    srf2051repo.executeBatch(batch)

    return srf01s
  }

  private fun createGdf10(protocolo: String, srf01: Srf01, dadosRps: DadosRps): Gdf10{
    val gdf10 = Gdf10(
      gdf10empresa = UserContext.safeUser.empId,
      gdf10dtEmiss = LocalDate.now(),
      gdf10hrEmiss = LocalTime.now(),
      gdf10sistema = GDF10SISTEMA_NFSE,
      gdf10tipoDoc = GDF10TIPODOC_DOCUMENTO,
      gdf10statusProc = GDF10STATUSPROC_APROVADO,
      gdf10protocolo = protocolo,
      gdf10cStat = "100",
      gdf10xMotivo = "NFSe emitida com sucesso",
      gdf10linkPdf = dadosRps.linkPDF,
      gdf10regOrigem = RegOrigem("Srf01", srf01.srf01id).toMap(),
    )

    srf2051repo.save(gdf10)
    return gdf10
  }

  fun extrairDadosRetornoItatiba(xml: String): List<DadosRps> {
    val xmlRoot = ElementXmlConverter.string2Element(xml)
    val nfses = xmlRoot.findChildNodes("Nfse") ?: return emptyList()

    val result = mutableListOf<DadosRps>()
    for (index in nfses.indices) {
      val nfse = nfses[index]
      val numeroNFSe = nfse.findChildValue("InfNfse.Numero")
        ?: throw ValidationException("Número da NFSe não encontrado na nota $index")
      val serieRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Serie")
        ?: throw ValidationException("Série do RPS não encontrado na nota $index")
      val numeroRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Numero")
        ?: throw ValidationException("Número do RPS não encontrado na nota $index")
      val protocolo = nfse.findChildValue("InfNfse.CodigoVerificacao")
        ?: throw ValidationException("CodigoVerificacao da NFSe não encontrado na nota $index")

      val niTomador = nfse.findFirstChildValue("TomadorServico.IdentificacaoTomador.CpfCnpj.Cnpj", "TomadorServico.IdentificacaoTomador.CpfCnpj.Cpf").orExc("TomadorServico não localizado na NFSe")
      val prestador = nfse.findFirstChildValue("Prestador.CpfCnpj.Cnpj", "Prestador.CpfCnpj.Cpf").orExc("Prestador não localizado na NFSe")
      val dataEmissao = nfse.findChildValue("InfNfse.DataEmissao")?.parseDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS").orExc("DataEmissao não localizado na NFSe")
      val link = "https://iss.itatiba.sp.gov.br/Exportar/NotaPrestado?tp=0&h=$protocolo&tomador=$niTomador&prestador=$prestador&data=${dataEmissao.format("ddMMyyyy")}&numero=$numeroNFSe"
      val valor = nfse.findChildValue("ValoresServico.ValorLiquidoNfse")?.toBigDecimal() ?:
        throw ValidationException("ValorLiquidoNfse não localizado na NFSe")

      result.add(DadosRps(numeroRps.toInt(), serieRps?.toInt(), numeroNFSe.toInt(), protocolo, niTomador, valor, link))
    }

    return result
  }
}

data class DadosRps(
  val numeroRps: Int,
  val serieRps: Int?,
  val numeroNFSe: Int,
  val protocolo: String,

  val niTomador: String,
  val valor: BigDecimal,

  val linkPDF: String,
)
