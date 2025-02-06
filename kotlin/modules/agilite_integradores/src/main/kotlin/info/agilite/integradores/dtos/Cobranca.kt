package info.agilite.integradores.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class Cobranca(
  val natureza: String,
  val cliente: Cliente,
  val itens: MutableList<ItemCobranca>,
  val formasPagamento: List<FormaPagamento>,
  var obs: String? = null,
  var agrupada: Boolean = false,
){
  fun total(): BigDecimal {
    return itens.sumOf { it.valor }
  }
}
data class ItemCobranca(
  val codigo: String,
  var descricao: String,
  val quantidade: Int,
  var valor: BigDecimal,
)

data class Cliente(
  var cnpj: String,
  val nome: String? = null,
  val endereco: String? = null,
  val numero: String? = null,
  val complemento: String? = null,
  val cidade: String? = null,
  val estado: String? = null,
  val cep: String? = null,
  var email: String? = null,
)

data class FormaPagamento(
  val nomeFormaPagamento: String,
  var dataVencimento: LocalDate,
  var valor: BigDecimal,
)
