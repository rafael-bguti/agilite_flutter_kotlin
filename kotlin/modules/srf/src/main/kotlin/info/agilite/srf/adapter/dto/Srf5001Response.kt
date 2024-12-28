package info.agilite.srf.adapter.dto

import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable
import java.math.BigDecimal
import java.time.LocalDate

@DbTable("srf012")
@DbSimpleJoin("INNER:srf012documento, INNER:scf021doc, srf012doc, srf01natureza, srf01dfeAprov")
class Srf5001Response (
  val cgs18codigo: String,

  val srf012valor: BigDecimal,

  val scf11data: LocalDate?,
  var scf02dtVenc: LocalDate,

  var srf01numero: Int,

  val gdf10linkPdf: String,
  val scf021remNumero: String,
)