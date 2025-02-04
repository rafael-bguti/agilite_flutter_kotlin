package info.agilite.boot.sdui.component

import info.agilite.boot.sdui.autocomplete.Option

class SduiColumn(
  val name: String,
  var label: String,
  var type: String,

  var width: SduiColumnWidth? = null,
  var options: List<Option>? = null,

  var mod: String? = null,
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