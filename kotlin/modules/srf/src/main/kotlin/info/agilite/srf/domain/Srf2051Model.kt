package info.agilite.srf.domain

import java.math.BigDecimal
import java.time.LocalDate

data class Srf2051Model(
  val qtdDocsPraProcessar: Int,
  val docsProcessados: List<Srf01Model>,
)

class Srf01Model(
  val srf01numero: Int,
  val srf01dtEmiss: LocalDate,
  val srf01nome: String,
  val srf01vlrTotal: BigDecimal,
)