package info.agilite.shared.events.srf

import info.agilite.shared.entities.srf.Srf01
import org.springframework.context.ApplicationEvent


data class Srf01EventPosSave (
  val origem: Any,
  val insert: Boolean,
  val srf01: Srf01,
): ApplicationEvent(origem)

data class Srf01EventPreSave (
  val origem: Any,
  val insert: Boolean,
  val srf01: Srf01,
): ApplicationEvent(origem)

data class Srf01EventPreDelete (
  val origem: Any,
  val srf01ids: List<Long>,
): ApplicationEvent(origem)