package info.agilite.boot.spring

object RequestContext {
  private val requestThreadLocal = ThreadLocal<RequestConfigs?>()

  internal fun putRequest(request: RequestConfigs) {
    requestThreadLocal.set(request)
  }

  val request: RequestConfigs?
    get() = requestThreadLocal.get()

  fun clear() {
    requestThreadLocal.remove()
  }
}

class RequestConfigs {
  var isTenantDefinedInDB = false
}