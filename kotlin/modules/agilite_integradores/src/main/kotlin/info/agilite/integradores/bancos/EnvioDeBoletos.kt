package info.agilite.integradores.bancos


import info.agilite.integradores.bancos.models.Boleto
import info.agilite.integradores.bancos.models.BoletoRecebido
import info.agilite.integradores.bancos.models.RetornoEnvioBoleto
import java.time.LocalDate

interface EnvioDeBoletos {
  fun enviarBoleto(boleto: Boleto): RetornoEnvioBoleto
  fun buscarBoletosPagos(vctoInicial: LocalDate, vctoFinal: LocalDate): List<BoletoRecebido>?
}