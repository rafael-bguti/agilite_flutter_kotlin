package info.agilite.boot.sdui.component

class SduiSizedBox(
  val width: Double? = null,
  val height: Double? = null,
  val child: SduiComponent? = null
) : SduiComponent() {
  companion object {
    fun shrink(): SduiSizedBox {
      return SduiSizedBox(width = 0.0, height = 0.0)
    }
  }
}