package info.agilite.boot.autocode.crud.models

data class CrudFilters(
  val page: Int,
  val pageSize: Int,
  val search: String?,
  val detailedFilters: Map<String, Any?>?,
)
