package info.agilite.boot.orm

class WhereAllEquals(
  override val params: Map<String, Any?>?,
) : WhereClause {
  override fun where(whereAndOr: String): String {
    if (params.orEmpty().isEmpty()) return ""
    return " $whereAndOr ${params!!.entries.joinToString(" AND ") { clause(it) }} "
  }

  private fun clause(entry: Map.Entry<String, Any?> ): String {
    if(entry.value == null) {
      return "${entry.key} IS NULL"
    }

    if(entry.value is String) {
      return "LOWER(${entry.key}) = LOWER(:${entry.key})"
    }

    return "${entry.key} = :${entry.key}"
  }

}
