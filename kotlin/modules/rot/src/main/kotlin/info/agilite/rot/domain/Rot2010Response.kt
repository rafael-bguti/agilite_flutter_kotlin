package info.agilite.rot.domain

data class Rot2010Response(
  val nome: String,
  val email: String,
  val empresa: String,
  val token: String,
  val refreshToken: String,
  var menu: List<FrontEndMenuItem>?,
)
