package info.agilite.integradores.bancos.models

import java.math.BigDecimal
import java.time.LocalDate

data class Boleto(
  val seuNumero: String,
  val valorNominal: BigDecimal,
  val dataVencimento: LocalDate,
  val numDiasAgenda: Int,
  val pagador: Pagador,
  val multa: MoraMulta,
  val mora: MoraMulta,
  val mensagem: Mensagem? = null,
)

enum class TipoPessoa {
  FISICA,
  JURIDICA,
}
data class Pagador(
  val cpfCnpj: String,
  val nome: String,
  val tipoPessoa: TipoPessoa,
  val endereco: String,
  val numero: String,
  val bairro: String?,
  val cidade: String,
  val uf: String,
  val cep: String,
  val complemento: String? = null,
)

enum class TipoValor {
  PERCENTUAL,
  FIXO,
}
data class MoraMulta(
  val valor: BigDecimal,
  val tipo: TipoValor,
)

data class Mensagem(
  val linha1: String,
  val linha2: String,
  val linha3: String,
  val linha4: String,
  val linha5: String,
)