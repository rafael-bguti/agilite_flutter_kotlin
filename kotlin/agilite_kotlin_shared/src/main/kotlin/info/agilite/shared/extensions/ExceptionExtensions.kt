package info.agilite.shared.extensions


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
