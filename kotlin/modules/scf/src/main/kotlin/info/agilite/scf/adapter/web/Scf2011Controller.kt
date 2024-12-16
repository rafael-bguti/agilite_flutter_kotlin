package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.integradores.bancos.models.BoletoProcessado
import info.agilite.scf.application.Scf2011Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping

@RestMapping("/scf2011")
class Scf2011Controller(
  private val scf2011Service: Scf2011Service
) {

  @PostMapping
  @Transactional
  fun processarBoletosPagos(): List<BoletoProcessado> {
    return scf2011Service.processarBoletosPagos()
  }
}