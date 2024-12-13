package info.agilite.gdf.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.gdf.adapter.infra.Gdf2060Repository
import info.agilite.gdf.adapter.infra.Gdf20Repository
import info.agilite.gdf.application.Gdf2060Service
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RestMapping("/gdf2060")
class Gdf2060Controller(
  private val gdf2060repo: Gdf2060Repository,
  private val gdf2060Service: Gdf2060Service,
) {

  @GetMapping
  fun list() = gdf2060repo.listarLotes()

  @PostMapping(consumes = ["multipart/form-data"])
  @Transactional
  fun processResult(
    @RequestParam("file") file: MultipartFile,
    @RequestParam("gdf20id") gdf20id: Long,
  ) {
    if (file.isEmpty) {
      throw ClientException(HttpStatus.BAD_REQUEST, "O arquivo está vazio.")
    }

    gdf2060Service.processarRetornoLoteNFse(gdf20id, file.bytes, file.contentType)
  }
}