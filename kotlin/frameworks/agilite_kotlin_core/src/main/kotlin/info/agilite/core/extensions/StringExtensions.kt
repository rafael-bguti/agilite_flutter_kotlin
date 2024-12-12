package info.agilite.core.extensions

import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
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

fun String.encryptToPassword(steps: Int): String{
  return try {
    var chave = this
    val md: MessageDigest = MessageDigest.getInstance("SHA-256")
    for (i in 0 until steps) {
      val strSnh: ByteArray = chave.toByteArray()
      md.update(strSnh)
      chave = BigInteger(1, md.digest()).toString(16)
    }
    chave
  } catch (e: Exception) {
    throw RuntimeException("Erro ao criptografar texto", e)
  }
}

// ----- PARSERS -----
fun String.parseDate(pattern: String = "yyyyMMdd"): LocalDate {
  return LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
}
