package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.scf.adapter.infra.Scf02Repository
import info.agilite.shared.entities.scf.Scf02
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@RestMapping("/scf2010")
class Scf2010Controller(
  private val scf02repo: Scf02Repository,
) {

  @GetMapping
  fun listarBoletosParaEnviarBanco(): List<Scf02> {
    return scf02repo.localizarBoletosParaEnviarBanco()
  }

  @PostMapping
  fun enviarBoletosParaBanco(): String {
    return "Boletos enviados com sucesso"
  }
}