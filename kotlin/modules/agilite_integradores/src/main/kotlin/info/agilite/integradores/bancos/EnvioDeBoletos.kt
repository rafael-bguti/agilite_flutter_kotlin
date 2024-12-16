package info.agilite.integradores.bancos


import info.agilite.integradores.bancos.models.Boleto
import info.agilite.integradores.bancos.models.BoletoRecebido
import java.time.LocalDate

interface EnvioDeBoletos {
  fun enviarBoletos(boletos: List<Boleto>)
  fun buscarBoletosPagos(vctoInicial: LocalDate, vctoFinal: LocalDate): List<BoletoRecebido>?
}