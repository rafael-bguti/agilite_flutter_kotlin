package info.agilite.srf.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.integradores.dtos.Cobranca
import info.agilite.srf.application.Srf2030Service
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/srf2030")
class Srf2030Controller(
  private val srf2030Service: Srf2030Service,
) {

  @PostMapping
  @Transactional
  fun importarDocumentos(@RequestBody cobrancas: List<Cobranca>) {
    if (cobrancas.isEmpty()) {
      throw ClientException(HttpStatus.BAD_REQUEST, "O arquivo está vazio.")
    }

    srf2030Service.doImport(cobrancas)
  }
}