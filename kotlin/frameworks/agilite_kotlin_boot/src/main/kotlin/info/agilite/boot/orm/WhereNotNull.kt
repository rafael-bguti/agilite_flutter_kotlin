package info.agilite.boot.orm

class WhereNotNull(
  val field: String
) : Where {
  override val params: Map<String, Any?> = mapOf()

  override fun where(whereAndOr: String): String {
    return " $whereAndOr $field IS NOT NULL "
  }
}
