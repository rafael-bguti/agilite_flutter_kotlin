package info.agilite.shared.events

import info.agilite.shared.entities.srf.Srf01
import org.springframework.context.ApplicationEvent

class Srf01FiscalProcessadoEvent(
  val origem: Any,
  val srf01: Srf01,
  val protocoloPrefeitura: String,
): ApplicationEvent(origem)