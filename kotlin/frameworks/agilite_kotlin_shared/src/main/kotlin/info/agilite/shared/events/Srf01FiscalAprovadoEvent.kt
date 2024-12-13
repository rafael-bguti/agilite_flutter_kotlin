package info.agilite.shared.events

import org.springframework.context.ApplicationEvent

class Srf01FiscalAprovadoEvent(
  val origem: Any,
  val srf01ids: List<Long>,
): ApplicationEvent(origem)