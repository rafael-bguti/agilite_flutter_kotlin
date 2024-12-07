package info.agilite.boot.security

import info.agilite.core.json.JsonUtils
import info.agilite.core.model.ErrorEntity
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val USER_ID_KEY = "user_id"
const val USER_TOKEN_KEY = "user_token"

@Component
@Order(2)
class AuthenticationFilter(
  val userDetailService: UserDetailService
) : OncePerRequestFilter() {
  private val LOG = LoggerFactory.getLogger("JwtAuthenticationFilter.class")
  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val path = request.servletPath
    return path.startsWith("/api/public/")
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      val userId = request.getAttribute(USER_ID_KEY) as String
      val userToken = request.getAttribute(USER_TOKEN_KEY) as String

      if (userId.isNullOrBlank()) {
        sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
        return
      }

      val userDetails: UserDetail? = userDetailService.loadUserByUserId(userId.toLong())
      if (userDetails == null) {
        sendError(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED, request, response)
        return
      }

      if(userDetails.token != userToken){
        sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
        return
      }

      UserContext.putUser(userDetails)
    } catch (e: JwtException) {
      LOG.error("Error on JwtAuthenticationFilter", e)
      sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
      UserContext.clear()
      return
    } catch (e: Exception) {
      LOG.error("Error on JwtAuthenticationFilter", e)
      sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", request, response)
      UserContext.clear()
      return
    }

    filterChain.doFilter(request, response)
  }

  private fun sendError(status: Int, cause: String, request: HttpServletRequest, response: HttpServletResponse) {
    response.status = status
    response.contentType = MediaType.APPLICATION_JSON_VALUE

    response.outputStream.print(
      JsonUtils.toJson(
        ErrorEntity(cause = cause, path = request.servletPath)
      )
    )
    response.flushBuffer()
  }
}