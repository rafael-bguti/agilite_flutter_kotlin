package info.agilite.boot.autocode.autocomplete.models

data class KeyLabel(
  val key: Any,
  val label: String,
  val allData: Map<String, Any?>? = null,
)