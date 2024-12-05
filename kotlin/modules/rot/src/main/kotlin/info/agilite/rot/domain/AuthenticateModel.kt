package info.agilite.rot.domain

data class AuthenticateModel(
  val rot01tenant: String,

  val rot10id: Long,
  val rot10email: String,
  val rot10senha: String,
  var rot10token: String?,
  var rot10refreshToken: String?,
  val rot10roles: String?,
)