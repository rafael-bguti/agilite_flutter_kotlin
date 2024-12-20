package info.agilite.srf.application

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.security.UserContext
import info.agilite.boot.sse.SseEmitter
import info.agilite.cgs.adapter.infra.Cgs18Repository
import info.agilite.cgs.adapter.infra.Cgs38Repository
import info.agilite.cgs.adapter.infra.Cgs50Repository
import info.agilite.cgs.adapter.infra.Cgs80Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.cgs.Cgs50
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.entities.srf.Srf011
import info.agilite.shared.entities.srf.Srf012
import info.agilite.srf.adapter.infra.Srf01Repository
import info.agilite.srf.domain.SRF2030Doc
import org.springframework.http.HttpStatus
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

  fun doImport(content: String, contentType: String?) {
    lateinit var docs: List<SRF2030Doc>
    if(contentType?.equals("text/csv", true) == true) {
      docs = Srf2030CsvParser.parseFromCsv(content)
    }else{
      throw ClientException(HttpStatus.BAD_REQUEST, "Tipo de arquivo não informado.")
    }

    for(index in docs.indices) {
      sseEmitter.sendMessage("Importando documento $index/${docs.size}")
      importDoc(index, docs[index])
    }
  }

  private fun importDoc(index: Int, doc: SRF2030Doc) {
    val user = UserContext.safeUser
    val cgs18 = cgs18repo.findByNome(doc.nomeNatureza) ?: throw ValidationException("Natureza não encontrada: ${doc.nomeNatureza} no ${index}º documento")
    val cgs80 = cgs80repo.findByNi(doc.niEntidade) ?: throw ValidationException("Entidade não encontrada: ${doc.niEntidade} no ${index}º documento")

    val maxSrf01Numero = srf01repo.findMaxNumero()
    val itens = parseItens(index, doc)

    val srf01 = Srf01(
      srf01empresa = user.empId,
      srf01natureza = cgs18,
      srf01tipo = doc.tipo,
      srf01es = cgs18.cgs18es,
      srf01numero = maxSrf01Numero + 1,
      srf01serie = cgs18.cgs18serie,
      srf01dtEmiss = LocalDate.now(),
      srf01entidade = cgs80,
      srf01vlrTotal = BigDecimal(0),
      srf01obs = doc.observacoes,
    )

    srf01.srf011s = itens
    srf01.srf012s = parseFormaRecebimento(index, doc)
    try {
      srf01BaseService.save(srf01)
    }catch (e: ValidationException) {
      throw ValidationException("Erro ao importar ${index}º documento: ${e.message}")
    }
  }

  private fun parseItens(index: Int, doc: SRF2030Doc): Set<Srf011> {
    if(doc.itens.isEmpty()) {
      throw ValidationException("${index}º documento sem itens")
    }

    return doc.itens.map {
      val cgs50id = cgs50repo.findIdByCodigo(it.codigoItem) ?: throw ValidationException("Item não encontrado: ${it.codigoItem} no ${index}º documento")

      Srf011(
        srf011item = Cgs50(cgs50id),
        srf011descr = it.descrItem,
        srf011qtd = BigDecimal(it.quantidade),
        srf011vlrUnit = it.unitario,
      )
    }.toSet()
  }

  private fun parseFormaRecebimento(index: Int, doc: SRF2030Doc): Set<Srf012> {
    if(doc.formasRecebimento.isEmpty()) {
      throw ValidationException("${index}º documento sem formas de recebimento")
    }

    return doc.formasRecebimento.map {
      val cgs38id = cgs38repo.findIdByNome(it.nomeFormaRecebimento) ?: throw ValidationException("Forma de recebimento não encontrada: ${it.nomeFormaRecebimento} no ${index}º documento")

      Srf012(
        srf012forma = Cgs38(cgs38id),
        srf012parcela = 1,
        srf012dtVenc = it.dataVencimento,
        srf012valor = it.valor,
      )
    }.toSet()
  }
}

