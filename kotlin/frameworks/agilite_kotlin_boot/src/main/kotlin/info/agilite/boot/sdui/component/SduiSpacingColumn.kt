package info.agilite.boot.sdui.component

class SduiSpacingColumn(
  val children: List<SduiComponent>,
  val spacing: Double? = null,
  val crossAxisAlignment: CrossAxisAlignment? = null,
  id: String? = null,
) : SduiComponent(id)
