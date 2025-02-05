package info.agilite.boot.crud

data class CrudListResponse(
  val currentPage: Int,
  val pageSize: Int,

  val data: List<Map<String, Any?>>,

  var groups: List<CrudListGroup>? = null,
  var selectedGroupIndex: Int? = null,
)

data class CrudEditResponse(
  val data: Map<String, Any?>,
  val editable: Boolean = true,
)
