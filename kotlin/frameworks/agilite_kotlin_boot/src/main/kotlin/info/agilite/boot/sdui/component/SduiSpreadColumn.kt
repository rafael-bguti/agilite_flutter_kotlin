package info.agilite.boot.sdui.component

import info.agilite.boot.metadata.models.FieldTypeMetadata
import info.agilite.boot.sdui.autocomplete.Option

class SduiSpreadColumnComponent(
  val name: String,
  val label: String,
  val type: String,
  val width: SduiColumnWidth? = null,
  val options: List<Option>? = null,
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