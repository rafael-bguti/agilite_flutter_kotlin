package info.agilite.cas.adapter.web

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.spring.RestMapping
import info.agilite.cas.application.Cas2010Service
import info.agilite.cas.domain.Cas2010Model
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@RestMapping("/public/cas2010")
class Cas2010Controller(
  private val service: Cas2010Service
) {

  @PostMapping
  @Transactional
  fun login(@RequestBody base64Login: String): Cas2010Model {
    val decoded = String(Base64.getDecoder().decode(base64Login), Charsets.UTF_8)
    val split = decoded.split(":")
    if(split.size < 2) throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")

    val usuarioOuEmail = split[0]
    val senha = split.subList(1, split.size).joinToString(":")

    return service.login(usuarioOuEmail, senha)
  }
}