package info.agilite.boot.security
object UserContext {
  private val userThreadLocal = ThreadLocal<UserDetail?>()

  internal fun putUser(user: UserDetail?) {
    userThreadLocal.set(user)
  }

  val user: UserDetail?
    get() = userThreadLocal.get()

  val tenantId: String?
    get() = userThreadLocal.get()?.tenantId

  fun clear() {
    userThreadLocal.remove()
  }
}
