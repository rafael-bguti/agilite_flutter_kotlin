package info.agilite.server_core.metadata.models

data class FieldMetadataDto(
  val name: String,
  val type: String,
  val label: String,
  val req: Boolean,
  val size: Double?,

  //---- OPTIONS ----
  val options: List<FieldOptionMetadata>?,

  //--- Autocomplete ----
  val autocompleteColumnId: String?,
  val autocompleteColumnsView: String?,

  val validationQuery: String?,
)