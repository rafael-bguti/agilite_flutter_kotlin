package info.agilite.integradores.bancos.inter

import info.agilite.core.extensions.format
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.bancos.EnvioDeBoletos
import info.agilite.integradores.bancos.models.Boleto
import info.agilite.integradores.bancos.models.BoletoRecebido
import java.math.BigDecimal
import java.time.LocalDate

private const val PATH_BOLETO = "/cobranca/v3/cobrancas"
private const val SCOPE = "boleto-cobranca.read boleto-cobranca.write"

class IntegradorDeBoletosBancoInter(
  private val bancoConfig: BancoInterConfig
) : EnvioDeBoletos {
  override fun enviarBoletos(boletos: List<Boleto>) {
    val token = GetTokenBancoInter.execute(bancoConfig, SCOPE)
    println("Token: $token")
    //TODO
  }

  override fun buscarBoletosPagos(vctoInicial: LocalDate, vctoFinal: LocalDate): List<BoletoRecebido>? {
    val token = GetTokenBancoInter.execute(bancoConfig, SCOPE)

    val cobrancas = mutableListOf<BoletoRecebido>()
    buscarBoletosPaginados(0, token, vctoInicial, vctoFinal, cobrancas)

    if(cobrancas.isEmpty())return null
    return cobrancas
  }

  private fun buscarBoletosPaginados(pagina: Int, token: TokenBancoInter, vctoInicial: LocalDate, vctoFinal: LocalDate, cobrancas: MutableList<BoletoRecebido>) {
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
      buscarBoletosPaginados(pagina + 1, token, vctoInicial, vctoFinal, cobrancas)
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
