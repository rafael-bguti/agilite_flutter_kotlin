package info.agilite.server_core.metadata.models

data class FieldMetadata(
  val name: String,
  val attIndex: Int,
  val label: String,
  val type: FieldTypeMetadata,
  val size: Double,
  val required: Boolean = false,
  val foreignKeyEntity: String? = null,
  val options: List<FieldOptionMetadata>? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val validationQuery: String?, //Query de validação exemplo: required,min:10,max:100
  val showInCrudList: Boolean = false,
  val filterable: Boolean = false,
  val showInFkAutoComplete: Boolean = false,
)