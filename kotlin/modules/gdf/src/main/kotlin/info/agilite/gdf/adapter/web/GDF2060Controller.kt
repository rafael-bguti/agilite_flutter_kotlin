package info.agilite.gdf.adapter.web

import info.agilite.boot.spring.RestMapping
import org.springframework.web.bind.annotation.PostMapping

@RestMapping("/gdf2060")
class GDF2060Controller {

  @PostMapping(consumes = ["multipart/form-data"])
  fun processResult() {

  }
}