package info.agilite.scf.utils

import info.agilite.integradores.bancos.BancoConfig
import info.agilite.integradores.bancos.inter.BancoInterConfig
import info.agilite.shared.entities.cgs.Cgs38


fun Cgs38.toBancoConfig(): BancoConfig {
  //TODO obter a configuração a partir do Banco de Dados
  return BancoInterConfig(
    clientId = "2acbb5a0-24b9-4ca7-ba72-1057fe6a1826",
    clientSecret = "fab3206e-191b-4f69-ba3b-da381ba0e6a4",
    certPath = "C:/Lixo/BancoInter/certificado.crt",
    keyPath = "C:/Lixo/BancoInter/chave.key",
    homologando = false,
  )
}
