package info.agilite.scf.adapter.dto

import info.agilite.boot.orm.annotations.DbTable
import info.agilite.shared.entities.scf.Scf11
import java.time.LocalDate

@DbTable("scf02")
data class Scf2011RetornoDto(
  val scf021id: Long,
  val scf021remNumero: String,

  val scf02id: Long,
  val scf02tipo: Int,
  val scf02nossoNum: String?,
  var scf02dtPagto: LocalDate?,

  var scf02lancamento: Scf11?,
)