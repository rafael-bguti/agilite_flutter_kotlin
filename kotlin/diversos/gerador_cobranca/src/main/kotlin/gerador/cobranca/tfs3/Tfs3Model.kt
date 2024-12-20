package gerador.cobranca.tfs3

import gerador.cobranca.CODIGO_ITEM_TFS3
import gerador.cobranca.NATUREZA_TFS3
import info.agilite.integradores.dtos.Cliente
import info.agilite.integradores.dtos.Cobranca
import info.agilite.integradores.dtos.FormaPagamento
import info.agilite.integradores.dtos.ItemCobranca
import java.math.BigDecimal
import java.time.LocalDate

data class Tfs3Evento(
  var contrato: String,
  var cnpj: String,
  var cliente: String,
  var dtInicioCob: String,
  var obs: String,
  var valor: BigDecimal,
  var totalEventos: Int
) {
  fun toCobranca(): Cobranca {
    return Cobranca(
      natureza = NATUREZA_TFS3,
      cliente = Cliente(
        cnpj = cnpj,
        nome = cliente
      ),
      itens = listOf(
        ItemCobranca(
          codigo = CODIGO_ITEM_TFS3,
          descricao = obs,
          quantidade = totalEventos,
          valor = valor
        )
      ),
      formasPagamento = listOf(
        FormaPagamento(
          nomeFormaPagamento = "Boleto Inter",
          dataVencimento = LocalDate.now().withDayOfMonth(20),
          valor = valor
        )
      )
    )
  }
}