package info.agilite.scf.application

import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.security.UserContext
import info.agilite.cgs.adapter.infra.Cgs38Repository
import info.agilite.integradores.bancos.IntegradorBancoFactory
import info.agilite.integradores.bancos.models.BoletoProcessado
import info.agilite.integradores.bancos.models.BoletoRecebido
import info.agilite.scf.adapter.infra.Scf02Repository
import info.agilite.scf.utils.toBancoConfig
import info.agilite.shared.RegOrigem
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.scf.Scf021
import info.agilite.shared.entities.scf.Scf11
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class Scf2011Service(
  private val cgs38repo: Cgs38Repository,
  private val scf02repo: Scf02Repository,
) {
  fun processarBoletosPagos(cgs38id: Long): List<BoletoProcessado> {
    val cgs38 = cgs38repo.findToBaixarBoletosById(cgs38id) ?: throw Exception("Forma de pagamento não encontrada")

    val batch = BatchOperations()
    val result: MutableList<BoletoProcessado> = mutableListOf()
    val integrador = IntegradorBancoFactory.getIntegradorBoletos(cgs38.toBancoConfig())

    val dataInicial = cgs38.cgs38conta!!.cgs45dtUltProcRetorno ?: LocalDate.now().plusDays(-90)
    val dataFinal = LocalDate.now()

    val boletos = integrador.buscarBoletosPagos(dataInicial, dataFinal)
    if(boletos != null){
      baixarBoletosRecebidos(cgs38, boletos, batch, result)
    }

    cgs38.cgs38conta!!.cgs45dtUltProcRetorno = LocalDate.now()
    batch.updateChanges(cgs38.cgs38conta!!, 3)

    scf02repo.executeBatch(batch)

    return result
  }

  fun baixarBoletosRecebidos(cgs38: Cgs38, cobranca: List<BoletoRecebido>, batch: BatchOperations, resultado: MutableList<BoletoProcessado>) {
    val codigosRetornoDoBanco = cobranca.map { it.codigoSolicitacao }
    val listDocsProcessar = scf02repo.buscarDocumentosByScf021remNumero(codigosRetornoDoBanco, cgs38.cgs38conta!!.cgs45id) ?: emptyList()
    val mapDocsByRemNumero = listDocsProcessar.associateBy { it.scf021remNumero }
    val scf11ids = scf02repo.nextIds("scf11", listDocsProcessar.size)

    cobranca.forEach { cobranca ->
      val scf2011RetornoDto = mapDocsByRemNumero[cobranca.codigoSolicitacao]
      if(scf2011RetornoDto == null){
        resultado.add(BoletoProcessado(null, null, cobranca, false, "Boleto pago no banco, mas não encontrado no sistema"))
        return@forEach
      }

      if(scf2011RetornoDto.scf02lancamento != null){
        resultado.add(BoletoProcessado(
          scf2011RetornoDto.scf02nossoNum,
          scf2011RetornoDto.scf02dtVenc,
          cobranca,
          false,
          "Boleto pago no banco, mas já estava baixado no sistema"
        ))
        return@forEach
      }

      val scf11 = Scf11(
        scf11empresa = UserContext.safeUser.empId,
        scf11tipo = scf2011RetornoDto.scf02tipo,
        scf11conta = cgs38.cgs38conta!!,
        scf11data = cobranca.dataSituacao,
        scf11valor = cobranca.valorTotalRecebido,
        scf11hist = "Baixa do documento de número ${scf2011RetornoDto.scf02nossoNum} ",
        scf11regOrigem = RegOrigem("Scf02", scf2011RetornoDto.scf02id).toMap()
      )
      scf11.scf11id = scf11ids.next()
      batch.insert(scf11, 0)

      scf2011RetornoDto.scf02lancamento = scf11
      scf2011RetornoDto.scf02dtPagto = LocalDate.now()
      batch.update(scf2011RetornoDto, 1)

      val scf021 = Scf021(scf2011RetornoDto.scf021id).also {
        it.scf021dtBaixa = LocalDate.now()
      }

      batch.updateChanges(scf021, 2)
      resultado.add(BoletoProcessado(
        scf2011RetornoDto.scf02nossoNum,
        scf2011RetornoDto.scf02dtVenc,
        cobranca,
        true,
        "Baixa realizada com sucesso"))
    }
  }
}