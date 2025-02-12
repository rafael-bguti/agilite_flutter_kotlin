package info.agilite.boot.orm

class WhereSimple (
  val filter: String,
  override val params: Map<String, Any?>? = null,
) : Where {
  override fun where(whereAndOr: String): String {
    if (filter.isBlank()) return " $whereAndOr true "
    return " $whereAndOr $filter "
  }
}