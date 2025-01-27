package info.agilite.boot.sdui.autocomplete

import info.agilite.boot.spring.RestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/autocomplete")
class AutoCompleteController(
  private val service: AutoCompleteService
) {

  @PostMapping("/find")
  fun find(@RequestBody dto: AutoCompleteSearchDto): List<Option> {
    return service.find(dto)
  }
}