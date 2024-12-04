package info.agilite.boot.sse

import com.google.common.cache.CacheBuilder
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter


object SSeService {
  private val emitters = CacheBuilder.newBuilder()
    .expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES)
    .removalListener<String, SseEmitter> { it.value?.complete() }
    .build<String, SseEmitter>()

  fun register(uuid: String, emitter: SseEmitter) {
    emitters.put(uuid, emitter)
  }

  fun sendMsg(uuid: String, message: String) {
    emitters.getIfPresent(uuid)?.send("msg:$message")
  }

  fun complete(uuid: String): SseEmitter? {
    val emitter = emitters.getIfPresent(uuid) ?: return null

    emitter.complete()
    emitters.invalidate(uuid)

    return emitter
  }
}