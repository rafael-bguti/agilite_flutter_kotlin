package info.agilite.integradores.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class Cobranca(
  val natureza: String,
  val cliente: Cliente,
  val itens: List<ItemCobranca>,
  val formasPagamento: List<FormaPagamento>,
)
data class ItemCobranca(
  val codigo: String,
  val descricao: String,
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
  val dataVencimento: LocalDate,
  val valor: BigDecimal,
)
