package info.agilite.server_core.metadata.api

import info.agilite.server_core.metadata.models.FieldMetadataDto
import info.agilite.server_core.spring.RestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/public/metadata")
class MetadataController(
  val service: MetadataService,
) {

  @PostMapping("/load")
  fun getFieldsByNamesMetadata(@RequestBody entities: List<String>): List<FieldMetadataDto> {
    return service.loadMetadata(entities)
  }
}