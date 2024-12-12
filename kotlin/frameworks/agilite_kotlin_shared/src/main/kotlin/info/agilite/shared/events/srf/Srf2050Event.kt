package info.agilite.shared.events.srf

import org.springframework.context.ApplicationEvent

data class Srf2050EventLoteGerado (
  val origem: Any,
  val xml: String,
  val srf01ids: List<Long>,
): ApplicationEvent(origem)