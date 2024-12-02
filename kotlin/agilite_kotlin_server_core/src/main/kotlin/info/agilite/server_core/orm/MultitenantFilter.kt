package info.agilite.server_core.orm

import info.agilite.server_core.orm.repositories.DefaultRepository
import info.agilite.server_core.security.UserContext
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
@Order(3)
class MultitenantFilter(
  private val repository: DefaultRepository
) : OncePerRequestFilter() {
  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val path = request.servletPath
    return path.startsWith("/api/public/")
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    UserContext.user?.let {
      if(it.tenantId != null){
        repository.execute("SET search_path TO ${it.tenantId}")
      }
    }

    filterChain.doFilter(request, response)
  }
}
