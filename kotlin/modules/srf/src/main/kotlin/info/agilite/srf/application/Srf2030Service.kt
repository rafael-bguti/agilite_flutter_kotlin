package info.agilite.srf.application

import info.agilite.boot.security.UserContext
import info.agilite.boot.sse.SseEmitter
import info.agilite.cgs.adapter.infra.Cgs18Repository
import info.agilite.cgs.adapter.infra.Cgs38Repository
import info.agilite.cgs.adapter.infra.Cgs50Repository
import info.agilite.cgs.adapter.infra.Cgs80Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.numbersOnly
import info.agilite.integradores.dtos.Cobranca
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.cgs.Cgs50
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.entities.srf.Srf011
import info.agilite.shared.entities.srf.Srf012
import info.agilite.srf.adapter.infra.Srf01Repository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class Srf2030Service(
  private val srf01BaseService: Srf01BaseService,
  private val srf01repo: Srf01Repository,
  private val cgs18repo: Cgs18Repository,
  private val cgs38repo: Cgs38Repository,
  private val cgs80repo: Cgs80Repository,
  private val cgs50repo: Cgs50Repository,
  private val sseEmitter: SseEmitter
) {

  fun doImport(cobrancas: List<Cobranca>) {
    for (cobranca in cobrancas) {
      importCobranca(cobranca)
    }
  }

  fun importCobranca(cobranca: Cobranca) {
    val user = UserContext.safeUser
    val cgs18 = cgs18repo.findByCodigo(cobranca.natureza) ?: throw ValidationException("Natureza não encontrada: ${cobranca.natureza}")
    val cgs80 = cgs80repo.findByNi(cobranca.cliente.cnpj.numbersOnly()) ?: throw ValidationException("Entidade não encontrada: ${cobranca.cliente.cnpj}")

    val maxSrf01Numero = srf01repo.findMaxNumero()

    val srf01 = Srf01(
      srf01empresa = user.empId,
      srf01natureza = cgs18,
      srf01tipo = cgs18.cgs18tipo,
      srf01es = cgs18.cgs18es,
      srf01numero = maxSrf01Numero + 1,
      srf01serie = cgs18.cgs18serie,
      srf01dtEmiss = LocalDate.now(),
      srf01entidade = cgs80,
      srf01vlrTotal = BigDecimal(0),
      srf01obs = cobranca.obs,
    )

    srf01.srf011s = parseItens(cobranca)
    srf01.srf012s = parseFormaRecebimento(cobranca)
    try {
      srf01BaseService.save(srf01)
    }catch (e: ValidationException) {
      throw ValidationException("Erro ao importar Documento do cliente ${cobranca.cliente.nome} - ${e.message}")
    }
  }

  private fun parseItens(cobranca: Cobranca): Set<Srf011> {
    if(cobranca.itens.isEmpty()) {
      throw ValidationException("Documento do cliente ${cobranca.cliente.nome} sem itens")
    }

    return cobranca.itens.map {
      val cgs50id = cgs50repo.findIdByCodigo(it.codigo) ?: throw ValidationException("Item de código ${it.codigo} não localizado")

      Srf011(
        srf011item = Cgs50(cgs50id),
        srf011descr = it.descricao,
        srf011qtd = BigDecimal(it.quantidade),
        srf011vlrUnit = it.valor,
      )
    }.toSet()
  }

  private fun parseFormaRecebimento(cobranca: Cobranca): Set<Srf012> {
    if(cobranca.formasPagamento.isEmpty()) {
      throw ValidationException("Documento do cliente ${cobranca.cliente.nome} sem formas de recebimento")
    }

    return cobranca.formasPagamento.map {
      val cgs38id = cgs38repo.findIdByNome(it.nomeFormaPagamento) ?: throw ValidationException("Forma de recebimento não encontrada: ${it.nomeFormaPagamento}")

      Srf012(
        srf012forma = Cgs38(cgs38id),
        srf012parcela = 1,
        srf012dtVenc = it.dataVencimento,
        srf012valor = it.valor,
      )
    }.toSet()
  }
}

