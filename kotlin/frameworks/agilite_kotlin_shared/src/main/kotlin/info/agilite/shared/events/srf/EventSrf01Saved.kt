package info.agilite.shared.events.srf

import info.agilite.shared.entities.srf.Srf01
import org.springframework.context.ApplicationEvent


data class Srf01SavedEvent (
  val origem: Any,
  val insert: Boolean,
  val srf01: Srf01,
): ApplicationEvent(origem)
