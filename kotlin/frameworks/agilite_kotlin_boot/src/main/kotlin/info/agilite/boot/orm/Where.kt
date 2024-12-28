package info.agilite.boot.orm

interface Where{
  fun where(whereAndOr: String = " WHERE "): String
  val params: Map<String, Any?>?
}

