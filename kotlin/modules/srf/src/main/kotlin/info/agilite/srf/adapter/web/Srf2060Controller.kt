package info.agilite.srf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.srf.adapter.dto.Srf2060Dto
import info.agilite.srf.adapter.infra.Srf2060Repository
import org.springframework.web.bind.annotation.GetMapping

@RestMapping("srf2060")
class Srf2060Controller(
  val repository: Srf2060Repository
) {

  @GetMapping
  fun listarDocumentosParaEnviarEmail(): List<Srf2060Dto>{
    return repository.buscarDadosDocumentosParaEnviarEmail()
  }

}