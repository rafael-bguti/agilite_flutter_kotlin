package info.agilite.srf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.boot.sse.HEADER_SSE_UID_NAME
import info.agilite.srf.adapter.infra.Srf2050Repository
import info.agilite.srf.application.SRF2050Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@RestMapping("/srf2050")
class Srf2050Controller(
  private val srf2050repo: Srf2050Repository,
  private val srF2050Service: SRF2050Service,
) {
  @GetMapping
  fun loadDocsToExport(): List<Srf2050EmitirDto> {
    return srf2050repo.findsNFSeToExport()
  }

  @PostMapping(produces = ["application/xml"])
  @Transactional
  fun emitirLoteXmlPrefeitura(
    @RequestBody(required = false) srf01ids: List<Long>?,
    @RequestHeader(name = HEADER_SSE_UID_NAME, required = false) sseUid: String?,
  ): String {
    return srF2050Service.emitirLoteXmlPrefeitura(srf01ids, sseUid ?: "")
  }
}