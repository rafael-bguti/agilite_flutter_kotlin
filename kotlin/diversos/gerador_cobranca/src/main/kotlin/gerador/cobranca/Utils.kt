package gerador.cobranca

import info.agilite.integradores.dtos.Cliente
import info.agilite.integradores.dtos.Cobranca
import info.agilite.integradores.dtos.FormaPagamento
import info.agilite.integradores.dtos.ItemCobranca
import java.math.BigDecimal
import java.time.LocalDate

class Utils {
  companion object {
    fun buildCobranca(
      natureza: String,
      cnpj: String,
      codigoItem: String,
      descrItem: String,
      valor: BigDecimal,
      forma: String = NOME_FORMA_PAGAMENTO_BOLETO,
      obs: String? = null,
      vencimento: LocalDate = LocalDate.now().withDayOfMonth(20)
    ): Cobranca {
      return Cobranca(
        natureza = natureza,
        cliente = Cliente(cnpj = cnpj),
        itens = mutableListOf(ItemCobranca(codigo = codigoItem, descricao = descrItem, quantidade = 1, valor = valor)),
        obs = obs,
        formasPagamento = listOf(
          FormaPagamento(
            nomeFormaPagamento = forma,
            dataVencimento = vencimento,
            valor = valor
          )
        )
      )
    }
  }
}