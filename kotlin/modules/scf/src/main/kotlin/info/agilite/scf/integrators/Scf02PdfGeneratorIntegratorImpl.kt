package info.agilite.scf.integrators

import info.agilite.core.exceptions.ValidationException
import info.agilite.integradores.bancos.IntegradorBancoFactory
import info.agilite.scf.infra.Scf02PdfGeneratorRepository
import info.agilite.scf.utils.toBancoConfig
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.integrators.Scf02AnexoCobranca
import info.agilite.shared.integrators.Scf02GeradorPDFToEmail
import org.springframework.stereotype.Service

@Service
class Scf02PdfGeneratorIntegratorImpl(
  val repo: Scf02PdfGeneratorRepository
) : Scf02GeradorPDFToEmail {
  override fun gerarAnexoCobranca(scf02ids: List<Long>): Map<Long, Scf02AnexoCobranca>? {
    if(scf02ids.isEmpty()) return null

    val dadosBoleto = repo.findScf02GerarPDF(scf02ids)
    if(dadosBoleto.isEmpty()) return null

    val result = mutableMapOf<Long, Scf02AnexoCobranca>()
    dadosBoleto.groupBy { it.cgs38id }.forEach { entry ->
      val cgs38 = repo.findById(Cgs38::class, entry.key)
      if(cgs38?.cgs38apiClientId == null){
        throw ValidationException("Forma de Pagamento não configurada para geração de boleto")
      }

      val bancoConfig = cgs38.toBancoConfig()
      val integrador = IntegradorBancoFactory.getIntegradorBoletos(bancoConfig)

      entry.value.forEach {
        var indexPdf = 0
        val bytesPDF = integrador.emitirPDF(it.scf021remNumero)

        result.put(it.scf02id, Scf02AnexoCobranca("Boleto_${++indexPdf}.pdf", bytesPDF, "application/pdf"))
      }
    }

    return result
  }
}

