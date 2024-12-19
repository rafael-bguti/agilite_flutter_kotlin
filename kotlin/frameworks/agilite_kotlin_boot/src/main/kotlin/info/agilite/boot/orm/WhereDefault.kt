package info.agilite.boot.orm

import info.agilite.boot.metadata.models.EntityMetadata

class WhereDefault (
  private val metadata: EntityMetadata,
) : Where {
  override fun where(whereAndOr: String): String {
    return " $whereAndOr ${AgiliteWhere.defaultWhere(metadata)} "
  }

  override val params: Map<String, Any?>? = null
}