package info.agilite.server_core.orm

class WhereSimple (
  val filter: String,
  override val params: Map<String, Any?>? = null,
) : WhereClause {
  override fun where(whereAndOr: String): String {
    if (filter.isBlank()) return ""
    return " $whereAndOr $filter "
  }
}