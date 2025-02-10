package info.agilite.boot.sdui.component

enum class FieldType {
  string,
  date,
  int,
  double,
}

class SduiTextField (
  val name: String,
  val type: FieldType,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
  val initialValue: String? = null,
  id: String? = null,
) : SduiComponent(id)
