package info.agilite.scf.listeners

import info.agilite.scf.adapter.infra.ScfListenerRepository
import info.agilite.scf.application.ScfBaseService
import info.agilite.shared.entities.cgs.CGS18SCF_NA_APROVACAO_FISCAL
import info.agilite.shared.events.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class ScfListenSrf01FiscalAprovadoEvent(
  private val repo: ScfListenerRepository,
  private val scfBaseService: ScfBaseService,
) {

  @EventListener
  fun onSrf01FiscalAprovadoEvent(event: Srf01FiscalProcessadoEvent) {
    val srf01 = event.srf01
    repo.inflate(srf01, "srf01natureza")
    if(srf01.srf01natureza.cgs18scf != CGS18SCF_NA_APROVACAO_FISCAL) return

    scfBaseService.gerarLancamentosAPartirDoSrf01(srf01)
  }
}
