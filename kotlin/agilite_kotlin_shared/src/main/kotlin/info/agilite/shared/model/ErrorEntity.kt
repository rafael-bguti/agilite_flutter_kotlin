package info.agilite.shared.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

data class ErrorEntity(
  val id: String = UUID.randomUUID().toString(),
  val utc: String = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME),
  val path: String,
  val cause: String? = null,
)
