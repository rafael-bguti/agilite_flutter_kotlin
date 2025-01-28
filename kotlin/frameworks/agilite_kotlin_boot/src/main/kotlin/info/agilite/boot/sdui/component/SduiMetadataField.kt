package info.agilite.boot.sdui.component

class SduiMetadataField (
  val name: String,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
  id: String? = null,
) : SduiComponent(id)