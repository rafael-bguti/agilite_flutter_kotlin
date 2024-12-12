package info.agilite.boot.sse

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

const val MIN_INTERVAL_SEND_MS = 1000

class SseCommand(
  private val emitter: SseEmitter
) {
  private var lastSendMs: Long = 1L
  fun send(message: String) {
    if (System.currentTimeMillis() - lastSendMs < MIN_INTERVAL_SEND_MS) {
      return
    }
    lastSendMs = System.currentTimeMillis()
    emitter.send(message)
  }

  fun complete() {
    emitter.complete()
  }
}