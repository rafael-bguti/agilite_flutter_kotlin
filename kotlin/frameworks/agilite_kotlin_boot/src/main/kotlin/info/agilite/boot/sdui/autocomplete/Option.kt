package info.agilite.boot.sdui.autocomplete

data class Option(
  val key: Any?,
  val label: String,
  val allData: Map<String, Any?>? = null,
)