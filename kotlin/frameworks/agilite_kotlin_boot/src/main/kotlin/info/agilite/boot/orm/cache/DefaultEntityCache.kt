package info.agilite.boot.orm.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalNotification
import info.agilite.core.extensions.doInLock
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.security.UserContext
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock

typealias RemovalListener = (tenant: String, entity: AbstractEntity) -> Unit

//TODO mudar o evento de remoção adicionando q o evento é remove all
object DefaultEntityCache {
  private val lock = ReentrantReadWriteLock()
  private val read: Lock = lock.readLock()
  private val write: Lock = lock.writeLock()

  private val removalListeners = mutableListOf<RemovalListener>()

  private val cache: Cache<String, AbstractEntity> = CacheBuilder.newBuilder()
    .maximumSize(800)
    .expireAfterAccess(15, TimeUnit.MINUTES)
    .removalListener { onInvalidate(it) }
    .build()

  fun put(value: AbstractEntity) {
    write.doInLock {
      val cacheKey = computeKey(value.javaClass.simpleName, value.id)
      cache.put(cacheKey, value)
    }
  }

  fun <T> get(className: String, id: Long): T? {
    return read.doInLock {
      val cacheKey = computeKey(className, id)
      cache.getIfPresent(cacheKey) as T?
    }
  }

  fun <T: AbstractEntity> getOrPut(tableName: String, id: Long, defaultValue: () -> T?): T? {
    val result = get(tableName,id) as T?
    if(result != null)return result
    val value = defaultValue()
    if(value != null){
      put(value)
    }
    return value
  }

  fun invalidate(className: String, id: Long) {
    write.doInLock {
      val cacheKey = computeKey(className, id)
      cache.invalidate(cacheKey)
    }
  }

  fun invalidateAll() {
    write.doInLock {
      cache.invalidateAll()
    }
  }

  private fun onInvalidate(notification: RemovalNotification<String, AbstractEntity>) {
    val entity = notification.value
    val tenantName = UserContext.user?.tenantId ?: "root"
    removalListeners.forEach {
      it(tenantName, notification.value as AbstractEntity)
    }
  }

  //Key = schema+table+id
  private fun computeKey(entityName: String, id: Long?): String {
    if(id == null) throw Exception("Id não informado")
    val tenantName = UserContext.user?.tenantId ?: "root"

    val sb = StringBuilder()
    sb.append(tenantName.lowercase()).append(".")
      .append(entityName.lowercase()).append(".")
      .append(id)

    return sb.toString()
  }
}
