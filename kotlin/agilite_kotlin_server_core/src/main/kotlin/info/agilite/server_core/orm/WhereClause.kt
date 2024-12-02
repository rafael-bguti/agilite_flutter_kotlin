package info.agilite.server_core.orm

interface WhereClause{
  fun where(whereAndOr: String = " WHERE "): String
  val params: Map<String, Any?>?
}