package info.agilite.server_core.autocode.autocomplete.models

import info.agilite.server_core.orm.WhereSimple

data class AutoCompleteSearchDto(
  val autocompleteFieldName: String,
  val query: String?,
  val defaultWhere: WhereSimple?,
  val ids: List<Long>?,
)

