package info.agilite.server_core.autocode.crud.models

data class CrudListData(
  val page: Int,
  val count: Long,
  val rows: List<Map<String, Any?>>,
)
