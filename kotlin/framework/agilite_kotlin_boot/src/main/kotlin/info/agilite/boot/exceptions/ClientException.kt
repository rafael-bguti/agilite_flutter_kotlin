package info.agilite.boot.exceptions

import org.springframework.http.HttpStatus

class ClientException(
  val statusCode: HttpStatus,
  val statusText: String = statusCode.reasonPhrase
) : RuntimeException(statusText)
