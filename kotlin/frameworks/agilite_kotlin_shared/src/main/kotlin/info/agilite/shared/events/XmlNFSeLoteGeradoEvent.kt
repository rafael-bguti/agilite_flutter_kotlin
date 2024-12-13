package info.agilite.shared.events

import org.springframework.context.ApplicationEvent

data class XmlNFSeLoteGeradoEvent (
  val origem: Any,
  val xml: String,
  val srf01ids: List<Long>,
): ApplicationEvent(origem)