package info.agilite.boot.sdui.component

class SduiPadding (
  val top: Double = 0.0,
  val right: Double = 0.0,
  val bottom: Double = 0.0,
  val left: Double = 0.0,
  val child: SduiComponent,
  id: String? = null,
): SduiComponent(id)