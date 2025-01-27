package info.agilite.srf.application

import info.agilite.core.exceptions.ValidationException
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.events.Srf01SavedEvent
import info.agilite.srf.infra.Srf01Repository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class Srf01BaseService(
  private val srf01repo: Srf01Repository,
  private val eventPublish: ApplicationEventPublisher,
) {

  fun save(srf01: Srf01) {
    totalizar(srf01)
    validarPreSave(srf01)

    val isInsert = srf01.id == null
    srf01repo.save(srf01)
    eventPublish.publishEvent(Srf01SavedEvent(this, isInsert, srf01))

    if(srf01.isChanged){
      srf01repo.updateChanges(srf01)
    }
  }

  private fun totalizar(srf01: Srf01){
    srf01.srf01vlrTotal = srf01.srf011s?.sumOf { it.srf011vlrTotal } ?: BigDecimal.ZERO
  }

  private fun validarPreSave(srf01: Srf01){
    if(srf01.srf011s.isNullOrEmpty()) throw ValidationException("Documento sem itens")

    if(!srf01.srf012s.isNullOrEmpty()){
      val totalFormas = srf01.srf012s!!.sumOf { it.srf012valor }
      if(totalFormas != srf01.srf01vlrTotal) throw ValidationException("Total das formas de recebimento/pagamento diferente do total do documento")
    }
  }
}