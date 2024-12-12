package info.agilite.scf.integrations

import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.orm.repositories.EmptyIdsList
import info.agilite.boot.orm.repositories.IdsList
import info.agilite.boot.security.UserContext
import info.agilite.core.exceptions.ValidationException
import info.agilite.scf.adapter.infra.ScfFromSrf01IntegrationRepository
import info.agilite.shared.RegOrigem
import info.agilite.shared.entities.cgs.*
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.scf.Scf11
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.entities.srf.Srf012
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.shared.events.srf.Srf01EventPosSave
import info.agilite.shared.events.srf.Srf01EventPreDelete
import info.agilite.shared.events.srf.Srf01EventPreSave
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class ScfFromSrf01Integration(
  private val repo: ScfFromSrf01IntegrationRepository,
  private val batchOperations: BatchOperations,
) {
  @EventListener
  fun onSrf01EventPreSave(event: Srf01EventPreSave) {
    ValidationProcessor(repo).validatePreSaved(event)
  }

  @EventListener
  fun onSrf01EventPreDelete(event: Srf01EventPreDelete) {
    ValidationProcessor(repo).validatePreDelete(event)
  }

  @EventListener
  fun onSrf01EventPosSave(event: Srf01EventPosSave) {
    CreateSFCProcessor(repo, batchOperations).process(event)
  }
}

private class ValidationProcessor(
  private val repo: ScfFromSrf01IntegrationRepository,
) {
  fun validatePreSaved(event: Srf01EventPreSave) {
    val srf01 = event.srf01
    if (!event.insert) {
      repo.inflate(srf01, "srf012s")

      if(srf01.srf012s.isNullOrEmpty())return
      if(srf01.srf01integracaoScf != INTEGRACAO_OK) return

      val srf012ids = repo.findSrf012ids(srf01.srf01id!!)
      validarAlteracaoNosDocumento(srf012ids)

      repo.deletarScf02(srf012ids)
    }
  }

  fun validatePreDelete(event: Srf01EventPreDelete) {
    val srf012ids = repo.findSrf012ids(event.srf01ids)
    validarAlteracaoNosDocumento(srf012ids)
  }

  fun validarAlteracaoNosDocumento(srf012ids: List<Long>){
    if(srf012ids.isEmpty()) return

    validarSeExistemLancamentosFinanceiros(srf012ids)
    validarSeExisteRemessaBancaria(srf012ids)
  }

  private fun validarSeExistemLancamentosFinanceiros(srf012Ids: List<Long>) {
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

private class CreateSFCProcessor(
  private val repo: ScfFromSrf01IntegrationRepository,
  private val batchOperations: BatchOperations,
) {
  fun process(event: Srf01EventPosSave) {
    val srf01 = event.srf01

    if (srf01.srf012s?.isNotEmpty() == true) {
      gerarNovosScf02s(srf01)
      repo.executeBatch(batchOperations)
    }
  }

  private fun gerarNovosScf02s(srf01: Srf01) {
    repo.inflate(srf01, "srf01natureza, srf012s.srf012forma")

    val novosIdsScf02 = repo.nextIds("scf02", srf01.srf012s!!.size)
    val novosIdsScf11 = loadScf11Ids(srf01.srf012s!!)
    srf01.srf012s!!.forEachIndexed { _, srf012 ->
      val cgs38 = srf012.srf012forma!!

      if (cgs38.cgs38gerar == CGS38GERAR_GERAR_EM_ABERTO || cgs38.cgs38gerar == CGS38GERAR_GERAR_QUITADO) {
        val scf02 = gerarNovoDocumentoFinanceiro(novosIdsScf02.next(), srf01, srf01.srf01natureza!!, srf012, cgs38)
        if (cgs38.cgs38gerar == CGS38GERAR_GERAR_QUITADO) {
          val scf11 = gerarNovoLancamentoFinanceiro(novosIdsScf11.next(), srf01, srf01.srf01natureza!!, srf012, cgs38, scf02)
          scf02.scf02lancamento = scf11

          batchOperations.insert(scf11, 0)
        }

        batchOperations.insert(scf02, 1)
      } else {
        throw NotImplementedError("Tipo de lançamento não implementado - ${cgs38.cgs38gerar}")
      }
    }

    srf01.srf01integracaoScf = INTEGRACAO_OK
  }

  private fun loadScf11Ids(srf012s: Set<Srf012>): IdsList {
    val srf012sGerarQuitados = srf012s.filter { it.srf012forma?.cgs38gerar == CGS38GERAR_GERAR_QUITADO }
    if(srf012sGerarQuitados.isEmpty()) return EmptyIdsList()

    return repo.nextIds("scf11", srf012sGerarQuitados.size)
  }

  private fun gerarNovoDocumentoFinanceiro(
    novoScf02id: Long,
    srf01: Srf01,
    cgs18: Cgs18,
    srf012: Srf012,
    cgs38: Cgs38
  ): Scf02 {
    return Scf02().apply {
      scf02id = novoScf02id
      scf02empresa = UserContext.safeUser.empId
      scf02tipo = cgs38.cgs38tipo
      scf02forma = cgs38
      scf02entidade = srf01.srf01entidade
      scf02dtEmiss = LocalDate.now()
      scf02dtVenc = srf012.srf012dtVenc
      scf02hist = criarHistorico(srf01, cgs18)
      scf02valor = srf012.srf012valor
      scf02regOrigem = RegOrigem("Srf012", srf012.srf012id!!).toMap()
    }
  }

  private fun gerarNovoLancamentoFinanceiro(novoScf11id: Long, srf01: Srf01, cgs18: Cgs18, srf012: Srf012, cgs38: Cgs38, scf02: Scf02): Scf11 {
    return Scf11().apply {
      scf11id = novoScf11id
      scf11empresa = UserContext.safeUser.empId
      scf11tipo = cgs38.cgs38tipo
      scf11conta = cgs38.cgs38conta!! //TODO atenção no cadastro de Forma de pagamento deve validar se a conta corrente é obrigatória quando for pra gerar quitado
      scf11data = srf012.srf012dtVenc
      scf11valor = srf012.srf012valor
      scf11hist = criarHistorico(srf01, cgs18)
      scf11regOrigem = RegOrigem("Scf02", scf02.scf02id!!).toMap()
    }

  }

  private fun criarHistorico(srf01: Srf01, cgs18: Cgs18): String {
    val tipoDocLabel = CGS18TIPO.options?.find { it.value == cgs18.cgs18tipo }?.label
    return "Criado a partir do documento '$tipoDocLabel' ${srf01.srf01numero}"
  }
}

