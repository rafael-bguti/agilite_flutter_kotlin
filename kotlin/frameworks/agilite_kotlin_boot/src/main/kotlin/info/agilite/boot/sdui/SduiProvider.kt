package info.agilite.boot.sdui

import info.agilite.boot.sdui.component.SduiComponent

interface SduiProvider {
  fun createSduiComponent(request: SduiRequest): SduiComponent
}