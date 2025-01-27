package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.scf.infra.Scf2010Dto
import info.agilite.scf.infra.Scf2010Repository
import info.agilite.scf.application.Scf2010Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestMapping("/scf2010")
class Scf2010Controller(
  private val scf2010Service: Scf2010Service,
  private val scf2010Repository: Scf2010Repository,
) {

  @GetMapping
  fun listarBoletosParaEnviarBanco(): List<Scf2010Dto> {
    return scf2010Repository.listarBoletosQueDevemSerEnviadosAoBanco()
  }

  @PostMapping
  fun enviarBoletosParaBanco(@RequestBody scf02ids: List<Long>) {
    scf2010Service.enviarBoletosParaBanco(scf02ids)
  }
}