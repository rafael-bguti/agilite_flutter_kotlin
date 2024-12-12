package info.agilite.srf.adapter.web

import info.agilite.boot.orm.annotations.DbTable
import java.math.BigDecimal
import java.time.LocalDate

@DbTable("Srf01")
data class Srf2050EmitirDto (
  val srf01id: Long,
  val srf01numero: Int,
  val srf01dtEmiss: LocalDate,
  val srf01nome: String,
  val srf01vlrTotal: BigDecimal,
  val srf01obs: String?,
)