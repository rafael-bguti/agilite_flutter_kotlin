package info.agilite.boot.sdui.component

import info.agilite.boot.sdui.autocomplete.Option

class SduiColumn(
  val name: String,
  val label: String,
  val type: String,

  val width: SduiColumnWidth? = null,
  val maxWidth: Double? = null,

  val options: List<Option>? = null,
  val mod: String?,
)

enum class SduiColumnWidthType {
  fixed,
  flex,
  byCharCount
}

class SduiColumnWidth(
  val type: SduiColumnWidthType,
  val width: Double
)