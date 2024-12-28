package info.agilite.core.extensions

import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.min


fun String.numbersOnly(): String {
  return this.replace(Regex("[^0-9]"), "")
}

fun String.localCapitalize () : String {
  return this.replaceFirstChar { this.first().uppercase() }
}

fun String.localDecapitalize () : String {
  return this.replaceFirstChar { this.first().lowercase()}
}

fun String?.nullIfEmpty () : String? {
  return if(this.isNullOrEmpty()) null else this
}

fun String.removeFromLast(count: Int): String {
  if(count >= this.length) return ""

  return this.substring(0, this.length - count)
}

fun String.substringBetween (delimiterStart: String, delimiterEnd: String ) : String {
  var start = this.indexOf(delimiterStart, ignoreCase = true)
  var end = this.indexOf(delimiterEnd, ignoreCase = true)
  if (start == -1) start = 0
  if (end == -1) end = this.length

  return this.substring(start + delimiterStart.length, end)
}

fun String.splitToList(delimiter: String = ",", trim: Boolean = true, ignoreBlank: Boolean = true): List<String> {
  return this.split(delimiter).map { it.trim() }.filter { it.isNotBlank() }
}

fun String.splitToSet(delimiter: String = ",", trim: Boolean = true, ignoreBlank: Boolean = true): Set<String> {
  return this.split(delimiter).map { it.trim() }.filter { it.isNotBlank() }.toSet()
}

fun String?.concatIfNotNull(prefix: String = "", suffix: String = "", defaultWhenNull: String = ""): String {
  if(this.isNullOrEmpty()) return defaultWhenNull
  return "$prefix$this$suffix"
}

fun String.substr(start: Int, length: Int): String {
  val sb = StringBuffer()
  val end = min(start + length, this.length)
  for (i in start until end) {
    sb.append(this.get(i))
  }
  return sb.toString()
}

fun String.maxLenght(max: Int): String {
  return if (this.length > max) this.substring(0, max) else this
}

// ----- PARSERS -----
fun String.parseDate(pattern: String = "yyyy-MM-dd"): LocalDate {
  return LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
}

fun String.parseDateTime(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): LocalDateTime {
  val normalize = if(this.contains('.')) {
    this.substringBefore('.') + "." + this.substringAfter('.').padEnd(3, '0').take(3)
  } else {
    this
  }

  return LocalDateTime.parse(normalize, DateTimeFormatter.ofPattern(pattern))
}
