package info.agilite.srf.domain

import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.shared.entities.cgs.Cgs15
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.gdf.Gdf10
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.srf.Srf01

@DbTable("srf01")
@DbSimpleJoin("srf01entidade, srf01natureza, INNER:cgs18modeloEmail")
data class Srf2060Mail(
  val cgs18id: Long,
  val cgs18nome: String,

  val cgs15id: Long,
  val cgs15nome: String,
  val cgs15titulo: String,

  val cgs80email: String?,

  val srf01id: Long,
  val srf01nome: String,
){
  fun validarPraEnviarEmail(): String? {
    if(cgs80email == null) return "Email não informado para o documento ${srf01nome}"
    return null
  }
}

class Srf2060Doc(
  val srf01: Srf01,
  val cgs80: Cgs80,
  val gdf10: Gdf10,
  val cgs15: Cgs15,
  val scf02s: MutableList<Scf02>
)