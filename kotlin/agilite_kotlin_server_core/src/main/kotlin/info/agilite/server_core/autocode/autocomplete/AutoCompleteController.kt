package info.agilite.server_core.autocode.autocomplete

import info.agilite.server_core.autocode.autocomplete.models.AutoCompleteSearchDto
import info.agilite.server_core.autocode.autocomplete.models.KeyLabel
import info.agilite.server_core.spring.RestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/autocomplete")
class AutoCompleteController(
  private val service: AutoCompleteService
) {

  @PostMapping("/find")
  fun find(@RequestBody dto: AutoCompleteSearchDto): List<KeyLabel> {
    return service.find(dto)
  }
}