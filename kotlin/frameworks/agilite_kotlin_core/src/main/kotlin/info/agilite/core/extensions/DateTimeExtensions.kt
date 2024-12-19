package info.agilite.core.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.format(pattern: String = "yyyy-MM-dd"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDateTime.format(pattern: String = "yyyy-MM-dd'T'HH:mm:yy"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.formatPtBR(): String {
    return this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun LocalDateTime.formatISO(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
}