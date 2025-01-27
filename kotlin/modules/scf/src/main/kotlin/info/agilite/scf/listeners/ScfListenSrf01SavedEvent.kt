package info.agilite.scf.listeners

import info.agilite.core.exceptions.ValidationException
import info.agilite.scf.infra.ScfListenerRepository
import info.agilite.scf.application.ScfFromSrfBaseService
import info.agilite.shared.entities.cgs.CGS18SCF_AO_CRIAR_O_DOCUMETO
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.events.INTEGRACAO_NAO_EXECUTAR
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.shared.events.Srf01SavedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class ScfListenSrf01SavedEvent(
  private val repo: ScfListenerRepository,
  private val scfFromSrfBaseService: ScfFromSrfBaseService,
) {
  @EventListener
  fun onSrf01SavedEvent(event: Srf01SavedEvent) {
    val srf01 = event.srf01
    if(srf01.srf01integracaoScf == INTEGRACAO_NAO_EXECUTAR) return

    if(!event.insert && srf01.srf01integracaoScf == INTEGRACAO_OK) {
      deletarLancamentosNoUpdateDoSrf01(srf01)

      scfFromSrfBaseService.gerarLancamentosAPartirDoSrf01(srf01)
    }

    if(event.insert && srf01.srf01natureza.cgs18scf == CGS18SCF_AO_CRIAR_O_DOCUMETO) {
      scfFromSrfBaseService.gerarLancamentosAPartirDoSrf01(srf01)
    }
  }

  private fun deletarLancamentosNoUpdateDoSrf01(srf01: Srf01) {
    repo.inflate(srf01, "srf012s")

    if (srf01.srf012s.isNullOrEmpty()) return
    if (srf01.srf01integracaoScf != INTEGRACAO_OK) return

    val srf012ids = srf01.srf012s!!.map { it.srf012id }

    validarSeExistemLancamentosQuitadosFinanceiros(srf012ids)
    validarSeExisteRemessaBancaria(srf012ids)

    repo.deletarScf02(srf012ids)
  }

  private fun validarSeExistemLancamentosQuitadosFinanceiros(srf012Ids: List<Long>) {
    val qtdLancamentos = repo.buscarQtdScf02Quitados(srf012Ids)

    if (qtdLancamentos > 0) {
      throw ValidationException("Existem lançamentos financeiros vinculados a este documento.")
    }
  }

  private fun validarSeExisteRemessaBancaria(srf012Ids: List<Long>) {
    val qtdDocsRemessa = repo.buscarQtdScf021(srf012Ids)

    if (qtdDocsRemessa > 0) {
      throw ValidationException("Existem documentos de remessa bancária vinculados a este documento.")
    }
  }
}
