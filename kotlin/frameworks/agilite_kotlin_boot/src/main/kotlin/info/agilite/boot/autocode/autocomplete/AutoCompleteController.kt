package info.agilite.boot.autocode.autocomplete

import info.agilite.boot.autocode.autocomplete.models.AutoCompleteSearchDto
import info.agilite.boot.autocode.autocomplete.models.KeyLabel
import info.agilite.boot.spring.RestMapping
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