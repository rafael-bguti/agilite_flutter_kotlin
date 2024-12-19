package info.agilite.boot.orm

class WhereAnd(
  private vararg val wheres: Where
) : Where {
  override fun where(whereAndOr: String): String {
    return " $whereAndOr ${wheres.joinToString(" AND ") { " ${it.where("")} " } } "
  }
  override val params: Map<String, Any?>? = null
}
