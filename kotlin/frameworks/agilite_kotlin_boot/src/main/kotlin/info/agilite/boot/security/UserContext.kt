package info.agilite.boot.security
object UserContext {
  private val userThreadLocal = ThreadLocal<UserDetail?>()

  internal fun putUser(user: UserDetail?) {
    userThreadLocal.set(user)
  }

  val user: UserDetail?
    get() = userThreadLocal.get()

  val safeUser : UserDetail
    get() = userThreadLocal.get() ?: throw RuntimeException("Usuário não autenticado.")

  val tenantId: String?
    get() = userThreadLocal.get()?.tenantId

  fun clear() {
    userThreadLocal.remove()
  }
}
