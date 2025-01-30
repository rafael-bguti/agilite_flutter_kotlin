package info.agilite.boot.spring

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(0)
class AgiliteRequestContextFilter() : OncePerRequestFilter() {
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      RequestContext.putRequest(RequestConfigs())
      filterChain.doFilter(request, response)
    } finally {
      RequestContext.clear()
    }
  }
}