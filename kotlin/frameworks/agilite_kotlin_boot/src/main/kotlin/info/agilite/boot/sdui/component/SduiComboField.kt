import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.SduiComponent

data class SduiComboField(
  val name: String,
  val options: List<Option>,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
) : SduiComponent()