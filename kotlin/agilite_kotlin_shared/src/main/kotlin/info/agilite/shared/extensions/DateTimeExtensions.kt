package info.agilite.shared.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.format(pattern: String = "yyyyMMdd"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.formatPtBR(): String {
    return this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun LocalDateTime.formatISO(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
}