package info.agilite.rot.adapter.infra.web

import info.agilite.rot.application.AutenticacaoService
import info.agilite.rot.domain.Rot2010Response
import info.agilite.boot.spring.RestMapping
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@RestMapping("/public/rot2010")
class Rot2010Controller(
  private val service: AutenticacaoService
) {

  @PostMapping(consumes = ["text/plain"])
  @Transactional
  fun logar(@RequestBody base64Login: String): Rot2010Response {


    val user = split[0]
    val password = split.subList(1, split.size).joinToString(":")

    return service.autenticar(user, password)
  }

}