package info.agilite.server_core.exceptions

import org.springframework.http.HttpStatus

class ClientException(
  val statusCode: HttpStatus,
  val statusText: String = statusCode.reasonPhrase
) : RuntimeException(statusText)
