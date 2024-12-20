package gerador.cobranca

import info.agilite.integradores.dtos.Cliente
import info.agilite.integradores.dtos.Cobranca
import info.agilite.integradores.dtos.FormaPagamento
import info.agilite.integradores.dtos.ItemCobranca
import java.math.BigDecimal
import java.time.LocalDate

class Utils {
  companion object {
    fun buildCobranca(natureza: String, cnpj: String, codigoItem: String, descrItem: String, valor: BigDecimal): Cobranca{
      return Cobranca(
        natureza = natureza,
        cliente = Cliente(cnpj = cnpj),
        itens = listOf(ItemCobranca(codigo = codigoItem, descricao = descrItem, quantidade = 1, valor = valor)),
        formasPagamento = listOf(FormaPagamento(nomeFormaPagamento = NOME_FORMA_PAGAMENTO_BOLETO, dataVencimento = LocalDate.now().withDayOfMonth(20), valor = valor))
      )
    }
  }
}