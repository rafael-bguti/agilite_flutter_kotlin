package info.agilite.boot.orm.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalNotification
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.security.UserContext
import info.agilite.core.extensions.orExc
import java.util.concurrent.TimeUnit

typealias InvalidateListener = (event: InvalidateEntityEvent) -> Unit

object DefaultEntityCache {
  private val removalListeners = mutableListOf<InvalidateListener>()

  private val cache: Cache<String, AbstractEntity> = CacheBuilder.newBuilder()
    .maximumSize(800)
    .expireAfterAccess(15, TimeUnit.MINUTES)
    .removalListener { onInvalidate(it) }
    .build()

  fun put(value: AbstractEntity) {

    cache.put(computeCacheKey(value.javaClass.simpleName, value.id.orExc("Não é permitido adicionar entidades sem ID no cache")), value)
  }

  fun <T> get(className: String, id: Long): T? {
    return cache.getIfPresent(computeCacheKey(className, id)) as T?
  }

  fun <T : AbstractEntity> getOrPut(tableName: String, id: Long, defaultValue: () -> T?): T? {
    val result = get(tableName, id) as T?
    if (result != null) return result
    val value = defaultValue()
    if (value != null) {
      put(value)
    }
    return value
  }

  fun invalidate(className: String, id: Long) {
    cache.invalidate(computeCacheKey(className, id))
  }

  fun invalidateAll() {
    cache.invalidateAll()
  }

  private fun onInvalidate(notification: RemovalNotification<String, AbstractEntity>) {
    val tenantName = UserContext.user?.tenantId ?: "root"
    removalListeners.forEach {
      it(InvalidateEntityEvent(tenantName, notification.value as AbstractEntity))
    }
  }

  //Key = schema+table+id
  private fun computeCacheKey(entityName: String, id: Long): String {
    val tenantName = UserContext.safeTentantId
    return computeCacheKey(entityName, id, tenantName)
  }
  private fun computeCacheKey(entityName: String, id: Long, tenantName: String): String {
    val sb = StringBuilder()
    sb.append(tenantName.lowercase()).append(".")
      .append(entityName.lowercase()).append(".")
      .append(id)

    return sb.toString().lowercase()
  }

  fun addInvalidateListener(listener: InvalidateListener) {
    removalListeners.add(listener)
  }
  fun removeInvalidateListener(listener: InvalidateListener) {
    removalListeners.remove(listener)
  }
}

class InvalidateEntityEvent(
  val tenant: String,
  val entity: AbstractEntity,
)