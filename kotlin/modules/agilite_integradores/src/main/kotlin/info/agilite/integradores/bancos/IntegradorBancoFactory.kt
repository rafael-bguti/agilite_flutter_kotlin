package info.agilite.integradores.bancos

import info.agilite.integradores.bancos.inter.BancoInterConfig
import info.agilite.integradores.bancos.inter.IntegradorDeBoletosBancoInter

object IntegradorBancoFactory {
  fun getIntegradorBoletos(config: BancoConfig): EnvioDeBoletos {
    return when (config::class) {
      BancoInterConfig::class -> IntegradorDeBoletosBancoInter(config as BancoInterConfig)
      else -> throw IllegalArgumentException("Banco ${config.javaClass.simpleName} não suportado")
    }
  }
}