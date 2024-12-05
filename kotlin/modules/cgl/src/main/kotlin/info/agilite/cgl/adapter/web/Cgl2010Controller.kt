package info.agilite.cgl.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.cgl.application.Cgl2010Service
import info.agilite.cgl.domain.Cgl2010Model
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@RestMapping("/public/cgl2010")
class Cgl2010Controller(
  private val service: Cgl2010Service
) {

  @PostMapping(consumes = ["text/plain"])
  @Transactional
  fun login(@RequestBody base64Login: String): Cgl2010Model {
    val decoded = String(Base64.getDecoder().decode(base64Login), Charsets.UTF_8)
    val split = decoded.split(":")
    if(split.size < 2) throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")

    val usuarioOuEmail = split[0]
    val senha = split.subList(1, split.size).joinToString(":")

    return service.login(usuarioOuEmail, senha)
  }
}