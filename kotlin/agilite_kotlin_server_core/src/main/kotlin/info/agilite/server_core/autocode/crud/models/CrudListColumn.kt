package info.agilite.server_core.autocode.crud.models

data class CrudListColumn(
  val name: String,
  val label: String,
  val type: String,
  val options: List<CrudListColumnOptions>?,
  val showOnList: Boolean,
  val showOnMoreFilters: Boolean,
  val charCountToWidth: Int,
  val tableName: String? = null,
)

data class CrudListColumnOptions(
  val key: Any,
  val label: String,
)