package info.agilite.server_core.metadata.models

import info.agilite.server_core.orm.WhereSimple

data class AutocompleteConfig(
  val field: FieldMetadata,

  val table: String,
  val columnsToSelect: String,
  val columnsToView: String? = null,
  val defaultWhere: WhereSimple? = null
)