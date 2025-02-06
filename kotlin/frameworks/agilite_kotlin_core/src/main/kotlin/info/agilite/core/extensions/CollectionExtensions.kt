package info.agilite.core.extensions

fun <T> List<T>?.nullIfEmpty() : List<T>? {
  return if(this.isNullOrEmpty()) null else this
}
fun <T> MutableList<T>.addOrReplace(t: T) {
  this.indexOf(t).let { if (it >= 0) this[it] = t else this.add(t) }
}
fun <T> Set<T>?.nullIfEmpty() : Set<T>? {
  return if(this.isNullOrEmpty()) null else this
}
fun Collection<*>?.isNotNullAndNotEmpty(): Boolean {
  return !this.isNullOrEmpty()
}