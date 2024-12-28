package info.agilite.integradores.bancos.inter

import info.agilite.integradores.bancos.BancoConfig

private const val urlProducao: String = "https://cdpj.partners.bancointer.com.br"
private const val urlHomologacao: String = "https://cdpj-sandbox.partners.uatinter.co"
class BancoInterConfig(
  val clientId: String,
  val clientSecret: String,
  val certData: String,
  val keyData: String,
  val homologando: Boolean,
  val contaCorrente: String ?= null,
) : BancoConfig {
  fun getUrl(): String = if (homologando) urlHomologacao else urlProducao
}