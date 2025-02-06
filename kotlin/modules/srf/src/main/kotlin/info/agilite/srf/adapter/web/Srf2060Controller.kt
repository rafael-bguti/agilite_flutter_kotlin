package info.agilite.srf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.boot.sse.SseEmitter
import info.agilite.srf.domain.Srf2060Mail
import info.agilite.srf.infra.Srf2060Repository
import info.agilite.srf.application.Srf2060Service
import info.agilite.srf.domain.Srf2060Filter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("srf2060")
class Srf2060Controller(
  val repository: Srf2060Repository,
  val service: Srf2060Service, //TODO alterar para emissão de evento assincrono de envio de email
  val sse: SseEmitter,
) {

  @PostMapping("listar")
  fun listarDocumentosParaEnviarEmail(@RequestBody filter: Srf2060Filter): List<Srf2060Mail> {
    return repository.buscarDadosDocumentosParaEnviarEmail(filter)
  }

  @PostMapping
  fun enviarEmails(@RequestBody srf2060dtos: List<Srf2060Mail>) {
    service.enviarEmailDocumentos(srf2060dtos)
  }
}