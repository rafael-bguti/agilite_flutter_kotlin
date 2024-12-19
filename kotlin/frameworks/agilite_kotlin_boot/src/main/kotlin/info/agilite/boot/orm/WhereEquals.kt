package info.agilite.boot.orm

import info.agilite.core.utils.MapUtils

class WhereEquals(
  private vararg val values: Any
) : Where {
  override val params: Map<String, Any?> = MapUtils.newStringMap(*values)

  override fun where(whereAndOr: String): String {
    return " $whereAndOr ${params.entries.joinToString(" AND ") { clause(it) }} "
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
