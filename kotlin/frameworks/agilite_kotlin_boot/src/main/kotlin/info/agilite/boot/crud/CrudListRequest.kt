package info.agilite.boot.crud

data class CrudListRequest(
  val currentPage: Int,
  val pageSize: Int,
  val search: String?,
  val customFilters: Map<String, Any?>?,
  val dialogMoreFiltersValue: Map<String, Any?>?,
  var groupIndex: Int?,
)
