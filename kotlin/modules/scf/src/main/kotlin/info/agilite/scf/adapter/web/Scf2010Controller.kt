package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.scf.adapter.infra.Scf02Repository
import info.agilite.scf.application.Scf2010Service
import info.agilite.shared.entities.scf.Scf02
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@RestMapping("/scf2010")
class Scf2010Controller(
  private val scf02repo: Scf02Repository,
  private val scf2010Service: Scf2010Service
) {

  @GetMapping
  fun listarBoletosParaEnviarBanco(): List<Scf02> {
    return scf02repo.listarBoletosQueDevemSerEnviadosAoBanco()
  }

  @PostMapping
  fun enviarBoletosParaBanco() {
    scf2010Service.enviarBoletosParaBanco()
  }
}