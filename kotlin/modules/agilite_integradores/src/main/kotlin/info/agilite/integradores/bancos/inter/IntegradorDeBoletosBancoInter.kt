package info.agilite.integradores.bancos.inter

import info.agilite.core.extensions.format
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.bancos.IntegradorBoletos
import info.agilite.integradores.bancos.models.Boleto
import info.agilite.integradores.bancos.models.BoletoRecebido
import info.agilite.integradores.bancos.models.RetornoEnvioBoleto
import java.time.LocalDate
import java.util.Base64

private const val PATH_BOLETO = "/cobranca/v3/cobrancas"
private const val SCOPE = "boleto-cobranca.read boleto-cobranca.write"

//TODO adicionar SSE
class IntegradorDeBoletosBancoInter(
  private val bancoConfig: BancoInterConfig
) : IntegradorBoletos {
  override fun enviarBoleto(boleto: Boleto): RetornoEnvioBoleto {
    val body = JsonUtils.toJson(boleto)
    val result = BancoInterHttpClientBuilder.callPost("${bancoConfig.getUrl()}$PATH_BOLETO", bancoConfig, SCOPE, body)
    return JsonUtils.fromJson(result, RetornoEnvioBoleto::class.java)
  }

  override fun buscarBoletosPagos(vctoInicial: LocalDate, vctoFinal: LocalDate): List<BoletoRecebido>? {
    val cobrancas = mutableListOf<BoletoRecebido>()
    buscarBoletosPaginados(0, vctoInicial, vctoFinal, cobrancas)

    if(cobrancas.isEmpty())return null
    return cobrancas
  }

  override fun emitirPDF(codigoSolicitacao: String): ByteArray {
    val response = BancoInterHttpClientBuilder.callGet("${bancoConfig.getUrl()}$PATH_BOLETO/$codigoSolicitacao/pdf", bancoConfig, SCOPE)
    val map = JsonUtils.fromJson(response, Map::class.java)

    return Base64.getDecoder().decode(map["pdf"] as String)
  }

  private fun buscarBoletosPaginados(pagina: Int, vctoInicial: LocalDate, vctoFinal: LocalDate, cobrancas: MutableList<BoletoRecebido>) {
    val requestParams = mapOf(
      "dataInicial" to vctoInicial.format(),
      "dataFinal" to vctoFinal.format(),
      "filtrarDataPor" to "VENCIMENTO",
      "situacao" to "RECEBIDO",
      "paginacao.paginaAtual" to "0",
      "ordenarPor" to "IDENTIFICADOR"
    )

    val response = BancoInterHttpClientBuilder.callGet("${bancoConfig.getUrl()}$PATH_BOLETO", bancoConfig, SCOPE, requestParams)
    val retornoConsulta = JsonUtils.fromJson(response, RetornoConsultaBancoInter::class.java)

    println(retornoConsulta)

    if(retornoConsulta.totalElementos > 0){
      cobrancas.addAll(retornoConsulta.cobrancas!!.map { it.cobranca })
    }
    if(!retornoConsulta.ultimaPagina){
      buscarBoletosPaginados(pagina + 1, vctoInicial, vctoFinal, cobrancas)
    }
  }
}

class RetornoConsultaBancoInter(
  val ultimaPagina: Boolean,
  val totalElementos: Int,
  val cobrancas: List<RetornoCobrancaBancoInter>?,
)

data class RetornoCobrancaBancoInter(
  val cobranca: BoletoRecebido,
)
