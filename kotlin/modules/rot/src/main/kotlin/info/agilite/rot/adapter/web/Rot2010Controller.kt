package info.agilite.rot.adapter.web

import info.agilite.rot.application.Rot2010Service
import info.agilite.rot.domain.Rot2010Response
import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@RestMapping("/public/rot2010")
class Rot2010Controller(
  private val service: Rot2010Service
) {

  @PostMapping(consumes = ["text/plain"])
  @Transactional
  fun logar(@RequestBody base64Login: String): Rot2010Response {
    val decoded = String(Base64.getDecoder().decode(base64Login), Charsets.UTF_8)
    val split = decoded.split(":")
    if(split.size < 2) throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")

    val user = split[0]
    val password = split.subList(1, split.size).joinToString(":")

    return service.autenticar(user, password)
  }

}