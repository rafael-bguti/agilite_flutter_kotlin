package info.agilite.srf.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.srf.adapter.infra.Srf2051Repository
import info.agilite.srf.application.Srf2051Service
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RestMapping("/srf2051")
class Srf2051Controller(
  private val srf2051Repository: Srf2051Repository,
  private val srf2051Service: Srf2051Service,
) {
  @GetMapping
  fun list() = srf2051Repository.findNFSeParaProcessar()

  @PostMapping(consumes = ["multipart/form-data"])
  @Transactional
  fun processResult(
    @RequestParam("file") file: MultipartFile,
  ) {
    if (file.isEmpty) {
      throw ClientException(HttpStatus.BAD_REQUEST, "O arquivo está vazio.")
    }

    srf2051Service.processarRetornoLoteNFse(file.bytes, file.contentType)
  }
}