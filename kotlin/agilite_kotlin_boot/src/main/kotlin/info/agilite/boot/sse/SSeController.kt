package info.agilite.boot.sse

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter


@RestController
class SSeController{
  @PostMapping(value = ["/public/messenger"])
  fun startMessenger(@RequestBody uuid: String): SseEmitter {
    val emitter = SseEmitter(-1L)
    SSeService.register(uuid, emitter)

    Thread {
      emitter.send("start:connected")
    }.start()

    return emitter
  }

  @PostMapping(value = ["/public/messenger/stop"], produces = [MediaType.ALL_VALUE], consumes = [MediaType.ALL_VALUE])
  fun stopMessenger(@RequestBody uuid: String): String {
    val result = SSeService.complete(uuid)
    return if (result != null) {
      "ok"
    } else {
      "not found"
    }
  }

}