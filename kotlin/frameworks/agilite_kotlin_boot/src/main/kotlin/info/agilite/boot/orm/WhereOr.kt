package info.agilite.boot.orm

class WhereOr(
  private vararg val wheres: Where
) : Where {
  override fun where(whereAndOr: String): String {
    return " $whereAndOr (${wheres.joinToString(" OR ") { " (${it.where("")}) " } }) "
  }
  override val params: Map<String, Any?>? = null
}
