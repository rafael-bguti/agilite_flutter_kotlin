package info.agilite.boot.security

import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

interface UserDetailCache {
  fun getUser(userId: Long): UserDetail?
  fun putUser(userId: Long, user: UserDetail)
  fun removeUser(userId: Long)
}

/***
 * Atenção esse é um cache simples deve ser usado apenas em pequenos projetos ou em testes
 * Para sistema maiores é recomendado usar um cache distribuído como Redis ou Memcached
 */
@Component
class SimpleUserDetailCache : UserDetailCache {
  val userCache = CacheBuilder.newBuilder()
    .expireAfterAccess(1, TimeUnit.HOURS)
    .maximumSize(200)
    .build<Long, UserDetail>()

  override fun getUser(userId: Long): UserDetail? {
    return userCache.getIfPresent(userId)
  }

  override fun putUser(userId: Long, user: UserDetail) {
    userCache.put(userId, user)
  }

  override fun removeUser(userId: Long) {
    userCache.invalidate(userId)
  }
}
