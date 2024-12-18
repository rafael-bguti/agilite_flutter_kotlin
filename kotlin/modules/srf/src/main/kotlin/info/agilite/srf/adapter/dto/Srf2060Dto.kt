package info.agilite.srf.adapter.dto

import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable

@DbTable("srf01")
@DbSimpleJoin("srf01entidade, srf01natureza, INNER:cgs18modeloEmail")
data class Srf2060Dto(
  val cgs18nome: String,

  val cgs15nome: String,
  val cgs15modelo: String,
  val cgs15titulo: String,

  val cgs80email: String,

  val srf01nome: String,
)