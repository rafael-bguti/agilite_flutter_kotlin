package info.agilite.boot.sse

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


const val HEADER_SSE_UID_NAME: String = "X-SSE-UID"
@Component
class SseEmitterFilter() : OncePerRequestFilter() {
  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val path = request.servletPath
    return path.startsWith("/api/public/")
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try{
      filterChain.doFilter(request, response)
    } finally {
      completeSse(request)
    }
  }

  private fun completeSse(request: HttpServletRequest) {
    val sseUid = request.getHeader(HEADER_SSE_UID_NAME)
    if(sseUid != null) {
      SSeService.complete(sseUid)
    }
  }
}
