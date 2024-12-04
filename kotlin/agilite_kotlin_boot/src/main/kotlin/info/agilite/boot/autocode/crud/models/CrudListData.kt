package info.agilite.boot.autocode.crud.models

data class CrudListData(
  val page: Int,
  val count: Long,
  val rows: List<Map<String, Any?>>,
)
