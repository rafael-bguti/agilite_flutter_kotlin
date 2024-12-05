package info.agilite.cgl.domain

import info.agilite.boot.autocode.FrontEndMenuItem

data class Cgl2010Model(
  val nome: String,
  val email: String,
  val empresa: String,
  val token: String,
  val refreshToken: String,
  var menu: List<FrontEndMenuItem>?,
)
