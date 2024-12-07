package info.agilite.boot.orm

internal data class TableAndDefaultSchema(
  val table: String,
  val defaultSchema: String? = null,

  val sql: String = defaultSchema?.let { "$it.$table" } ?: table
)
