package info.agilite.srf.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.boot.sse.HEADER_SSE_UID_NAME
import info.agilite.srf.application.Srf2030Service
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RestMapping("/srf2030")
class Srf2030Controller(
  private val srf2030Service: Srf2030Service,
) {

  @PostMapping(consumes = ["multipart/form-data"])
  @Transactional
  fun uploadFile(
    @RequestParam("file") file: MultipartFile,
    @RequestHeader(name = HEADER_SSE_UID_NAME, required = false) sseUid: String?,
  ) {
    if (file.isEmpty) {
      throw ClientException(HttpStatus.BAD_REQUEST, "O arquivo está vazio.")
    }
    srf2030Service.doImport(String(file.bytes), file.contentType, sseUid ?: "")
  }
}