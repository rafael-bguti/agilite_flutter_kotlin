package info.agilite.boot.orm.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import com.google.common.cache.RemovalNotification
import info.agilite.core.extensions.doInLock
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.core.utils.ReflectionUtils
import info.agilite.boot.security.UserContext
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.reflect.KClass

typealias RemovalListener = (entry: CacheEntryWrapper) -> Unit

object GlobalEntityCache {
  private val lock = ReentrantReadWriteLock()
  private val read: Lock = lock.readLock()
  private val write: Lock = lock.writeLock()

  private val removalListeners = mutableListOf<RemovalListener>()

  private val cache: Cache<String, CacheEntryWrapper> = CacheBuilder.newBuilder()
    .maximumSize(800)
    .expireAfterAccess(15, TimeUnit.MINUTES)
    .removalListener { onInvalidate(it) }
    .build()

  val keysBySchemaTableAndId = mutableMapOf<String, MutableSet<String>>()

  fun put(cacheName: String, schema: String, tableName: String, key: String, value: Any) {
    val id = ReflectionUtils.getIdValue(value, tableName) ?: throw RuntimeException("Entity without id is not cacheable")
    write.doInLock {
      val cacheKey = computeKey(cacheName, schema, tableName, key)
      cache.put(cacheKey, CacheEntryWrapper(cacheName, schema, tableName, key, id, value))

      val schemaTableAndIdKey = computeSchemaTableIdKey(schema, tableName, id)
      keysBySchemaTableAndId.getOrPut(schemaTableAndIdKey) { mutableSetOf() }.add(cacheKey)
    }
  }

  fun <T> get(cacheName: String, schema: String, tableName: String, key: String): T? {
    return read.doInLock {
      val cacheKey = computeKey(cacheName, schema, tableName, key)
      cache.getIfPresent(cacheKey)?.value as T?
    }
  }

  fun invalidate(cacheName: String, schema: String, tableName: String, key: String) {
    write.doInLock {
      val cacheKey = computeKey(cacheName, schema, tableName, key)
      cache.getIfPresent(cacheKey)?.let { entry ->
        removeKeysBySchemaTableAndId(entry)
        removalListeners.forEach { it(entry) }
        cache.invalidate(cacheKey)
      }
    }
  }

  fun invalidateById(schema: String, tableName: String, id: Long) {
    write.doInLock {
      val tableAndIdKey = computeSchemaTableIdKey(schema, tableName, id)
      keysBySchemaTableAndId[tableAndIdKey]?.forEach { key ->
        cache.getIfPresent(key)?.let { entry ->
          removalListeners.forEach { it(entry) }
          cache.invalidate(key)
        }
      }
      keysBySchemaTableAndId.remove(tableAndIdKey)
    }
  }

  fun invalidateByCacheName(cacheName: String) {
    val localCacheName = "$cacheName."
    write.doInLock {
      cache.asMap().filter { it.key.startsWith(localCacheName) }.forEach { entry ->
        removeKeysBySchemaTableAndId(entry.value)
        removalListeners.forEach { it(entry.value) }
        cache.invalidate(entry.key)
      }
    }
  }

  fun invalidateAll() {
    write.doInLock {
      cache.asMap().forEach { entry ->
        removalListeners.forEach { it(entry.value) }
      }

      cache.invalidateAll()
      keysBySchemaTableAndId.clear()
    }
  }

  private fun removeKeysBySchemaTableAndId(entry: CacheEntryWrapper) {
    val schemaTableAndIdKey = computeSchemaTableIdKey(entry.schema, entry.tableName, entry.id)
    val cacheKey = computeKey(entry.cacheName, entry.schema, entry.tableName, entry.key)

    keysBySchemaTableAndId[schemaTableAndIdKey]?.remove(cacheKey).let {
      if (keysBySchemaTableAndId[schemaTableAndIdKey]?.isEmpty() == true) {
        keysBySchemaTableAndId.remove(schemaTableAndIdKey)
      }
    }
  }

  private fun onInvalidate(notification: RemovalNotification<String, CacheEntryWrapper>) {
    if(notification.cause == RemovalCause.EXPIRED || notification.cause == RemovalCause.SIZE) {
      notification.value?.let { removeKeysBySchemaTableAndId(it) }
    }
  }

  private fun computeKey(cacheName: String, schema: String, tableName: String, key: String): String {
    val sb = StringBuilder()
    sb.append(cacheName).append(".")
    .append(schema).append(".")
    .append(tableName).append(".")
    .append(key)

    return sb.toString()
  }

  private fun computeSchemaTableIdKey(schema: String, tableName: String, id: Long): String {
    val sb = StringBuilder()
    sb.append(schema).append(".")
    .append(tableName).append(".")
    .append(id)

    return sb.toString()
  }

  fun addRemovalListener(listener: RemovalListener){
    removalListeners.add(listener)
  }

  fun removeRemovalListener(listener: RemovalListener){
    removalListeners.remove(listener)
  }
}

object DefaultEntityCache {
  private const val NAME: String = "global"
  fun <T> get(tableName: String, key: String): T? {
    return GlobalEntityCache.get(NAME, extractSchema(), tableName, key)
  }

  fun <T> getOrPut(tableName: String, key: String, defaultValue: () -> T?): T? {
    val result = GlobalEntityCache.get(NAME, extractSchema(), tableName, key) as T?
    if(result != null)return result
    val value = defaultValue()
    if(value != null){
      put(tableName, key, value)
    }
    return value
  }

  fun put(tableName: String, key: String, value: Any) {
    GlobalEntityCache.put(NAME, extractSchema(), tableName, key, value)
  }

  fun invalidate(tableName: String, key: String) {
    GlobalEntityCache.invalidate(NAME, extractSchema(), tableName, key)
  }

  fun invalidateById(tableName: String, id: Long) {
    GlobalEntityCache.invalidateById(extractSchema(), tableName, id)
  }

  fun invalidateThisCache() {
    GlobalEntityCache.invalidateByCacheName(NAME)
  }

  fun invalidateAllCaches() {
    GlobalEntityCache.invalidateAll()
  }

  private fun extractSchema(): String {
    return UserContext.tenantId ?: "public"
  }
}

class EntityCache(
  private val name: String,
  private val tableName: String,
){
  companion object {
    fun buildByClass(clazz: KClass<*>): EntityCache {
      val javaClass = clazz.java
      val tableName = EntityMappingContext.getTableAndSchema(javaClass).table
      return EntityCache(javaClass.simpleName, tableName)
    }
  }
  fun <T> get(key: String): T? {
    return GlobalEntityCache.get(name, extractSchema(), tableName, key)
  }

  fun <T> getOrPut(key: String, defaultValue: () -> T?): T? {
    val result = GlobalEntityCache.get(name, extractSchema(), tableName, key) as T?
    if(result != null)return result
    val value = defaultValue()
    if(value != null){
      put(key, value)
    }
    return value
  }

  fun put(key: String, value: Any) {
    if(value is AbstractEntity && value.getMetadata().oneToMany.isNotEmpty()){
      throw RuntimeException("Entity with oneToMany relationships cannot be cached on EntityCache, use custom cache instead")
    }
    GlobalEntityCache.put(name, extractSchema(), tableName, key, value)
  }

  fun invalidate(key: String) {
    GlobalEntityCache.invalidate(name, extractSchema(), tableName, key)
  }

  fun invalidateById(id: Long) {
    GlobalEntityCache.invalidateById(extractSchema(), tableName, id)
  }

  fun invalidateThisCache() {
    GlobalEntityCache.invalidateByCacheName(name)
  }

  fun invalidateAllCaches() {
    GlobalEntityCache.invalidateAll()
  }

  private fun extractSchema(): String {
    return UserContext.tenantId ?: "public"
  }
}

data class CacheEntryWrapper(
  val cacheName: String,
  val schema: String,
  val tableName: String,
  val key: String,
  val id: Long,
  val value: Any
)

