package info.agilite.server_core.security

class UserDetail(
  val userId: Long,
  val empId: Long,
  val tenantId: String,
  val token: String,
  val roles: String?,
)