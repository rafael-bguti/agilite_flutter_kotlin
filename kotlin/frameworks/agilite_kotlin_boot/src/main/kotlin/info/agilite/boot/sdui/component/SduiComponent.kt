package info.agilite.boot.sdui.component

abstract class SduiComponent(
  val id: String? = null
) {
  val widget: String
    get() = this.javaClass.simpleName
}

enum class WrapCrossAlignment {
  start,
  end,
  center
}
enum class CrossAxisAlignment {
  start,
  end,
  center,
  stretch,
  baseline,
}

