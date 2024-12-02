package info.agilite.shared.extensions

fun <T> List<T>?.nullIfEmpty() : List<T>? {
  return if(this.isNullOrEmpty()) null else this
}
fun <T> Set<T>?.nullIfEmpty() : Set<T>? {
  return if(this.isNullOrEmpty()) null else this
}
fun Collection<*>?.isNotNullAndNotEmpty(): Boolean {
  return !this.isNullOrEmpty()
}