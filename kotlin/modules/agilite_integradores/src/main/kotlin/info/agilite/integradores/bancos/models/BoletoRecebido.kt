package info.agilite.integradores.bancos.models

import java.math.BigDecimal
import java.time.LocalDate

data class BoletoProcessado (
  val numero: String?,
  val dataVencimento: LocalDate?,

  val recebimento: BoletoRecebido,

  val baixadoComSucesso: Boolean,
  val status: String,
)

data class BoletoRecebido(
  val codigoSolicitacao: String,
  val seuNumero: String,
  val situacao: String,
  val dataSituacao: LocalDate,
  val valorTotalRecebido: BigDecimal,
  val pagador: PagadorRecebido?,
)

data class PagadorRecebido (
  val nome: String?,
  val cpfCnpj: String?
)