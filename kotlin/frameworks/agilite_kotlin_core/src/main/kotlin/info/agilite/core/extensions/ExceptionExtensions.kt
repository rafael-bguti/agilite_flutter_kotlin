package info.agilite.core.extensions

import info.agilite.core.exceptions.ValidationException


inline fun <reified  T> Exception.unwrap(level: Int = 5): T? {
  var e: Exception? = this
  var i = 0
  while (e != null) {
    if (i > level) {
      return null
    }
    if (e is T) {
      return e
    }
    e = if(e.cause is Exception) e.cause as Exception else null
    i++
  }
  return null
}

fun <T> T?.orExc(message: String): T {
  if (this == null) {
    throw RuntimeException(message)
  }
  return this
}
