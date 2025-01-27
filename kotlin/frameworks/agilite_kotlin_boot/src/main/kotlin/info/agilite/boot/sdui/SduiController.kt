package info.agilite.boot.sdui

import SduiComboField
import info.agilite.boot.metadata.models.FieldTypeMetadata
import info.agilite.boot.metadata.models.tasks.TaskDescr
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.boot.spring.RestMapping
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@RestMapping("/sdui")
class SduiController(
  private val appContext: ApplicationContext,
) {

  @GetMapping("/{base}")
  fun getTask(@PathVariable("base") base64SduiRequest: String): SduiComponent {
    val sduiRequest = SduiRequest.fromBase64(base64SduiRequest)
    val sduiProvider = SduiProviderResolver.sduiProvider(sduiRequest, appContext)

    return sduiProvider.createSduiComponent(sduiRequest)
  }
}