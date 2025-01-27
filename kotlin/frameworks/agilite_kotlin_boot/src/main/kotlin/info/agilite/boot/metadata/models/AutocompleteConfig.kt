package info.agilite.boot.metadata.models

import info.agilite.boot.orm.Where

data class AutocompleteConfig(
  val field: FieldMetadata,

  val table: String,
  val columnsToSelect: String,
  val columnsToView: String? = null,
  val defaultWhere: Where? = null,

  val simpleJoin: String? = null,
)