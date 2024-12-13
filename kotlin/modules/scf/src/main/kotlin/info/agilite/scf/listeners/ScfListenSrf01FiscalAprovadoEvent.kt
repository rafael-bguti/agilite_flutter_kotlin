package info.agilite.scf.listeners

import info.agilite.scf.adapter.infra.ScfListenerRepository
import info.agilite.scf.application.ScfBaseService
import info.agilite.shared.events.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class ScfListenSrf01FiscalAprovadoEvent(
  private val repo: ScfListenerRepository,
  private val scfBaseService: ScfBaseService,
) {

  @EventListener
  fun onSrf01FiscalAprovadoEvent(event: Srf01FiscalAprovadoEvent) {
    repo.buscarSrf01GerarScfNaProvacaoFiscal(event.srf01ids).forEach {srf01 ->
      scfBaseService.gerarLancamentosAPartirDoSrf01(srf01)
      if(srf01.isChanged){
        repo.updateChanges(srf01)
      }
    }
  }
}
