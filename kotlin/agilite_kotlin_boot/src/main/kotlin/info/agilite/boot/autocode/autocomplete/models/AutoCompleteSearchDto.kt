package info.agilite.boot.autocode.autocomplete.models

import info.agilite.boot.orm.WhereSimple

data class AutoCompleteSearchDto(
  val autocompleteFieldName: String,
  val query: String?,
  val defaultWhere: WhereSimple?,
  val ids: List<Long>?,
)

