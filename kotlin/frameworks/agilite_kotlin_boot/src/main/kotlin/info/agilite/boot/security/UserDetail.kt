package info.agilite.boot.security

class UserDetail(
  val userId: Long,
  val empId: Long,
  val token: String,
  val roles: String?,

  val tenantId: String?,
)