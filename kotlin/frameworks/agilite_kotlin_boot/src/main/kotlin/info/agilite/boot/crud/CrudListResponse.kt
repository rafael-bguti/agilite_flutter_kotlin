package info.agilite.boot.crud

data class CrudListResponse(
  val currentPage: Int,
  val pageSize: Int,

  val data: List<Map<String, Any?>>,

  val groups: List<CrudListGroup>? = null,
  val selectedGroupIndex: Int? = null,
)
