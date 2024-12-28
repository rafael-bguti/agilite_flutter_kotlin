package info.agilite.scf.application

import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.orm.repositories.EmptyIdsList
import info.agilite.boot.orm.repositories.IdsList
import info.agilite.boot.security.UserContext
import info.agilite.scf.adapter.infra.ScfBaseRepository
import info.agilite.shared.RegOrigem
import info.agilite.shared.entities.cgs.*
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.scf.Scf11
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.entities.srf.Srf012
import info.agilite.shared.events.INTEGRACAO_NAO_EXECUTAR
import info.agilite.shared.events.INTEGRACAO_OK
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ScfFromSrfBaseService(
  private val scfRepo: ScfBaseRepository,
) {
  fun gerarLancamentosAPartirDoSrf01(srf01: Srf01) {
    scfRepo.inflate(srf01, "srf01natureza, srf012s")
    if(srf01.srf01integracaoScf == INTEGRACAO_NAO_EXECUTAR) return
    if(srf01.srf012s.isNullOrEmpty()) return

    val novosIdsScf02 = scfRepo.nextIds("scf02", srf01.srf012s!!.size)
    val novosIdsScf11 = generateNewsSrf01ids(srf01.srf012s!!)
    val batchOperations = BatchOperations()
    srf01.srf012s!!.forEach { srf012 ->
      val cgs38 = srf012.srf012forma

      if (cgs38.cgs38gerar == CGS38GERAR_GERAR_EM_ABERTO || cgs38.cgs38gerar == CGS38GERAR_GERAR_QUITADO) {
        val scf02 = gerarNovoDocumentoFinanceiro(novosIdsScf02.next(), srf01, srf01.srf01natureza, srf012, cgs38)

        if (cgs38.cgs38gerar == CGS38GERAR_GERAR_QUITADO) {
          val scf11 = gerarNovoLancamentoFinanceiro(novosIdsScf11.next(), srf01, srf01.srf01natureza, srf012, cgs38, scf02)
          scf02.scf02lancamento = scf11
          scf02.scf02dtPagto = LocalDate.now()

          batchOperations.insert(scf11, 0)
        }
        batchOperations.insert(scf02, 1)

        srf012.srf012documento = scf02
        batchOperations.updateChanges(srf012, 2)
      } else {
        throw NotImplementedError("Tipo de lançamento não implementado - ${cgs38.cgs38gerar}")
      }
    }
    scfRepo.executeBatch(batchOperations)

    srf01.srf01integracaoScf = INTEGRACAO_OK
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

  private fun gerarNovoLancamentoFinanceiro(
    novoScf11id: Long,
    srf01: Srf01,
    cgs18: Cgs18,
    srf012: Srf012,
    cgs38: Cgs38,
    scf02: Scf02
  ): Scf11 {
    return Scf11().apply {
      scf11id = novoScf11id
      scf11empresa = UserContext.safeUser.empId
      scf11tipo = cgs38.cgs38tipo
      scf11conta = cgs38.cgs38conta!! //TODO atenção no cadastro de Forma de pagamento deve validar se a conta corrente é obrigatória quando for pra gerar quitado
      scf11data = srf012.srf012dtVenc
      scf11valor = srf012.srf012valor
      scf11hist = criarHistorico(srf01, cgs18)
      scf11regOrigem = RegOrigem("Scf02", scf02.scf02id).toMap()
    }
  }

  private fun generateNewsSrf01ids(srf012s: Set<Srf012>): IdsList {
    val srf012sGerarQuitados = srf012s.filter { it.srf012forma?.cgs38gerar == CGS38GERAR_GERAR_QUITADO }
    if(srf012sGerarQuitados.isEmpty()) return EmptyIdsList()

    return scfRepo.nextIds("scf11", srf012sGerarQuitados.size)
  }

  private fun criarHistorico(srf01: Srf01, cgs18: Cgs18): String {
    val tipoDocLabel = CGS18TIPO.options?.find { it.value == cgs18.cgs18tipo }?.label
    return "Criado a partir do documento '$tipoDocLabel' ${srf01.srf01numero}"
  }
}