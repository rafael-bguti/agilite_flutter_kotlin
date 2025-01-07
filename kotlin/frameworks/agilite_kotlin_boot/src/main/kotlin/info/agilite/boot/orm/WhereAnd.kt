package info.agilite.boot.orm

class WhereAnd(
  private vararg val wheres: Where
) : Where {
  override fun where(whereAndOr: String): String {
    if(wheres.isEmpty()) return " $whereAndOr true "

    return " $whereAndOr ${wheres.joinToString(" AND ") { " ${it.where("")} " } } "
  }
  override val params: Map<String, Any?>?
    get() = wheres.mapNotNull { it.params }.fold(mutableMapOf()) { acc, map -> acc.putAll(map); acc }
}
