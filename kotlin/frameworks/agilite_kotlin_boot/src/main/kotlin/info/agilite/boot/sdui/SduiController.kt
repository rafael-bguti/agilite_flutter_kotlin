package info.agilite.boot.sdui

import info.agilite.boot.sdui.component.SduiComponent
import info.agilite.boot.sdui.component.SduiCrud
import info.agilite.boot.spring.RestMapping
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@RestMapping("/sdui")
class SduiController(
  private val appContext: ApplicationContext,
) {
  @Value("\${spring.profiles.active:default}")
  private val activeProfile: String? = null

  @GetMapping("/{base}")
  fun getTask(@PathVariable("base") base64SduiRequest: String): SduiComponent {
    val sduiRequest = SduiRequest.fromBase64(base64SduiRequest)
    val sduiProvider = SduiProviderResolver.sduiProvider(sduiRequest, appContext)

    return sduiProvider.createSduiComponent(sduiRequest)
  }

  @GetMapping("/crudForm/{taskName}")
  fun getCrudFormInDevMode(@PathVariable("taskName") taskName: String): SduiComponent {
    val sduiRequest = SduiRequest(taskName)
    val sduiProvider = SduiProviderResolver.sduiProvider(sduiRequest, appContext)
    val crud = sduiProvider.createSduiComponent(sduiRequest)

    if(crud !is SduiCrud || crud.formBody == null) {
      throw RuntimeException("This endpoint is only available for SduiCrud with formBody")
    }

    return crud.formBody!!;
  }
}