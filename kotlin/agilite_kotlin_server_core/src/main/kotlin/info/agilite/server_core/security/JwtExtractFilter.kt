package info.agilite.server_core.security

import info.agilite.shared.json.JsonUtils
import info.agilite.shared.model.ErrorEntity
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

const val TOKEN_EXPIRED_ERROR_MESSAGE = "token_expired"
const val TOKEN_NOT_FOUND_ERROR_MESSAGE = "token_not_found"
const val TOKEN_INVALID_ERROR_MESSAGE = "invalid_token"
const val UNAUTHORIZED = "unauthorized"

@Component
@Order(1)
class JwtExtractFilter(
  val jwtService: JwtService,
) : OncePerRequestFilter() {
  private val LOG = LoggerFactory.getLogger("JwtAuthenticationFilter.class")
  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val path = request.servletPath
    return path.startsWith("/api/public/")
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      val authHeader: String? = request.getHeader("Authorization")

      val jwt = validateAndExtractToken(authHeader, request, response)
        ?: return

      val userId = jwtService.extractUserIdByToken(jwt)
      if (userId.isNullOrBlank()) {
        sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
        return
      }

      request.setAttribute(USER_ID_KEY, userId)
      request.setAttribute(USER_TOKEN_KEY, jwt)
    } catch (e: JwtException) {
      LOG.error("Error on JwtAuthenticationFilter", e)
      sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
      return;
    } catch (e: Exception) {
      LOG.error("Error on JwtAuthenticationFilter", e)
      sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", request, response)
      return;
    }

    filterChain.doFilter(request, response)
  }

  private fun validateAndExtractToken(authHeader: String?, request: HttpServletRequest, response: HttpServletResponse): String? {
    if (authHeader.isNullOrBlank()) {
      sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_NOT_FOUND_ERROR_MESSAGE, request, response)
      return null
    }

    if(!authHeader.startsWith("Bearer ", ignoreCase = true)){
      sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
      return null
    }

    val jwt = authHeader.substring(7)
    if(jwt.isBlank()) {
      sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID_ERROR_MESSAGE, request, response)
      return null
    }

    if(jwtService.isTokenExpired(jwt)){
      sendError(HttpStatus.FORBIDDEN.value(), TOKEN_EXPIRED_ERROR_MESSAGE, request, response)
      return null
    }

    return jwt
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