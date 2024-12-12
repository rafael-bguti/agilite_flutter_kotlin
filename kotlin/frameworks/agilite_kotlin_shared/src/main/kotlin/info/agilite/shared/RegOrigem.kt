package info.agilite.shared

import info.agilite.core.model.LowerCaseMap

class RegOrigem(
  private val tab: String,
  private val id: Long,
) {
  fun toMap(): LowerCaseMap {
    return LowerCaseMap.of(
      mapOf(
        "tab" to tab.lowercase(),
        "id" to id,
      )
    )
  }
}
