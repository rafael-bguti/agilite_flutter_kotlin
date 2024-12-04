package info.agilite.boot.orm.repositories

open class IdsList internal constructor(val ids: MutableList<Long>) {

  open fun hasNext(): Boolean {
    return ids.isNotEmpty()
  }

  open fun next(): Long {
    return ids.removeFirst()
  }
}

class EmptyIdsList : IdsList(mutableListOf()) {
  override fun hasNext(): Boolean {
    return false
  }

  override fun next(): Long {
    throw NoSuchElementException()
  }
}