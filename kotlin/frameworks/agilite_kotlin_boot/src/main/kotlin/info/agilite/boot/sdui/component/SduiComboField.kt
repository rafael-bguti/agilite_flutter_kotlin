import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.SduiComponent

data class SduiComboField(
  val name: String,
  val options: List<Option>,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
) : SduiComponent() {
  companion object {
    fun buildListOptions(vararg values: Any?): List<Option> {
      if (values.size % 2 != 0) {
        throw IllegalArgumentException("The values must be a pair")
      }

      val result = mutableListOf<Option>()
      for (i in values.indices step 2) {
        result.add(Option(values[i], values[i + 1].toString()))
      }

      return result
    }
  }
}