package info.agilite.boot.orm

interface WhereClause{
  fun where(whereAndOr: String = " WHERE "): String
  val params: Map<String, Any?>?
}