package info.agilite.server_core.autocode.crud.models

data class CrudFilters(
  val page: Int,
  val pageSize: Int,
  val search: String?,
  val detailedFilters: Map<String, Any?>?,
)
