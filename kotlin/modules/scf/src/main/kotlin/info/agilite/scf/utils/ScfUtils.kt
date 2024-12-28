package info.agilite.scf.utils

import info.agilite.integradores.bancos.BancoConfig
import info.agilite.integradores.bancos.inter.BancoInterConfig
import info.agilite.shared.entities.cgs.Cgs38


fun Cgs38.toBancoConfig(): BancoConfig {
  if(cgs38apiClientId == null){
    throw Exception("Forma de pagamento/recebimento '${cgs38nome}' não possui configuração para acesso à API do banco")
  }
  return BancoInterConfig(
    clientId = cgs38apiClientId!!,
    clientSecret = cgs38apiClientSecret!!,
    certData = cgs38apiCert!!,
    keyData = cgs38apiKey!!,
    homologando = false,
  )
}
