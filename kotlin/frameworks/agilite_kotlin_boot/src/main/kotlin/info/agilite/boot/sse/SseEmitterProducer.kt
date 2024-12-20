package info.agilite.boot.sse

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@Configuration
class SseEmitterProducer {

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  fun sseEmitterCreator(@Autowired request: HttpServletRequest): SseEmitter {
    val ssuid = request.getHeader(HEADER_SSE_UID_NAME)
    return SseEmitter(ssuid)
  }
}