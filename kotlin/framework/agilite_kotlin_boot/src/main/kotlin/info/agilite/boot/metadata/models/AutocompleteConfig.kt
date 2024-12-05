package info.agilite.boot.metadata.models

import info.agilite.boot.orm.WhereSimple

data class AutocompleteConfig(
  val field: FieldMetadata,

  val table: String,
  val columnsToSelect: String,
  val columnsToView: String? = null,
  val defaultWhere: WhereSimple? = null
)