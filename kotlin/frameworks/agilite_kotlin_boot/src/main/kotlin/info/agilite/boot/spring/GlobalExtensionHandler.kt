package info.agilite.boot.spring

import info.agilite.core.exceptions.ValidationException
import info.agilite.core.model.ErrorEntity
import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.observability.ObserverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@ControllerAdvice
class GlobalExtensionHandler : ResponseEntityExceptionHandler() {
  @Autowired(required = true)
  var auditService: ObserverService? = null

  @ResponseBody
  @ExceptionHandler(ClientException::class)
  protected fun handleClientException(ex: ClientException, request: WebRequest): ResponseEntity<Any>? {
    return handleExceptionInternal(ex, ex.statusText, HttpHeaders(), ex.statusCode, request)
  }

  @ResponseBody
  @ExceptionHandler(ValidationException::class)
  protected fun handleValidationException(ex: ValidationException, request: WebRequest?): ResponseEntity<Any> {
    logarErroEmDev(ex)
    return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(ex.message)
  }

  @ResponseBody
  @ExceptionHandler(Exception::class)
  protected fun handleThrowable(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
    return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
  }

  override fun handleExceptionInternal(
    ex: Exception,
    body: Any?,
    headers: HttpHeaders,
    statusCode: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any>? {
    logarErroEmDev(ex)
    auditService!!.programmerAttention("Exception handled in GlobalExtensionHandler", ex)
    return super.handleExceptionInternal(ex, body, headers, statusCode, request)
  }

  override fun createResponseEntity(
    @Nullable body: Any?,
    headers: HttpHeaders,
    statusCode: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any> {
    val uuid = UUID.randomUUID().toString()
    //TODO Mandar para o Bugsnag, S3, etc
    val entityError = ErrorEntity(
      uuid,
      LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME),
      parsePath(request),
      body?.toString() ?: statusCode.value().toString()
    )

    return ResponseEntity(entityError, headers, statusCode)
  }

  private fun logarErroEmDev(exception: Exception) {
    if ("dev".equals(System.getProperty("shared.profiles.active"), ignoreCase = true)) {
      exception.printStackTrace()
    }
  }

  private fun parsePath(request: WebRequest): String {
    try {
      val desc = request.getDescription(false)
      return desc.replace("uri=", "")
    } catch (e: Exception) {
      return request.getDescription(false)
    }
  }
}
