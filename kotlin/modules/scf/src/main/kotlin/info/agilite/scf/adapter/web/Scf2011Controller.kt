package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.integradores.bancos.models.BoletoProcessado
import info.agilite.scf.application.Scf2011Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestMapping("/scf2011")
class Scf2011Controller(
  private val scf2011Service: Scf2011Service
) {

  @PostMapping("/{cgs38id}")
  @Transactional
  fun processarBoletosPagos(@PathVariable cgs38id: Long): List<BoletoProcessado> {
    return scf2011Service.processarBoletosPagos(cgs38id)
  }
}