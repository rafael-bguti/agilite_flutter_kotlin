package info.agilite.srf.domain

import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable

@DbTable("scf021")
@DbSimpleJoin("scf021doc, scf02forma")
class Srf2031BoletoModel (
  val cgs38id: Long,
  val cgs38apiClientId: String,
  val cgs38apiClientSecret: String,
  val cgs38apiCert: String,
  val cgs38apiKey: String,
)