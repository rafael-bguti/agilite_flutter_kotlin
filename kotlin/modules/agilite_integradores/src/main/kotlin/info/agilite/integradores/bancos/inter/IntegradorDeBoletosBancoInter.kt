package info.agilite.integradores.bancos.inter

import info.agilite.core.extensions.format
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.bancos.EnvioDeBoletos
import info.agilite.integradores.bancos.models.Boleto
import info.agilite.integradores.bancos.models.BoletoRecebido
import info.agilite.integradores.bancos.models.RetornoEnvioBoleto
import java.time.LocalDate

private const val PATH_BOLETO = "/cobranca/v3/cobrancas"
private const val SCOPE = "boleto-cobranca.read boleto-cobranca.write"

//TODO adicionar SSE
class IntegradorDeBoletosBancoInter(
  private val bancoConfig: BancoInterConfig
) : EnvioDeBoletos {
  override fun enviarBoleto(boleto: Boleto): RetornoEnvioBoleto {
    val body = JsonUtils.toJson(boleto)

    print(body)
    val result = BancoInterHttpClientBuilder.callPost("${bancoConfig.getUrl()}$PATH_BOLETO", bancoConfig, SCOPE, body)

    return JsonUtils.fromJson(result, RetornoEnvioBoleto::class.java)
  }

  override fun buscarBoletosPagos(vctoInicial: LocalDate, vctoFinal: LocalDate): List<BoletoRecebido>? {
    val cobrancas = mutableListOf<BoletoRecebido>()
    buscarBoletosPaginados(0, vctoInicial, vctoFinal, cobrancas)

    if(cobrancas.isEmpty())return null
    return cobrancas
  }

  private fun buscarBoletosPaginados(pagina: Int, vctoInicial: LocalDate, vctoFinal: LocalDate, cobrancas: MutableList<BoletoRecebido>) {
    val requestParams = mapOf(
      "dataInicial" to vctoInicial.format("yyyy-MM-dd"),
      "dataFinal" to vctoFinal.format("yyyy-MM-dd"),
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
