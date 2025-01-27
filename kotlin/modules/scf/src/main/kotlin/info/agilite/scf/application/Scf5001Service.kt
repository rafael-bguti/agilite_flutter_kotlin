package info.agilite.scf.application

import info.agilite.integradores.bancos.IntegradorBancoFactory
import info.agilite.scf.infra.Scf5001Repository
import info.agilite.scf.utils.toBancoConfig
import org.springframework.stereotype.Service

@Service
class Scf5001Service(
  private val srf5001Repository: Scf5001Repository
) {
  fun gerarBoleto(tenantName: String, numRemessa: String): ByteArray {
    val cgs38 = srf5001Repository.buscarFormaRecebimentoPelaRemessa(tenantName, numRemessa)

    val integracao = IntegradorBancoFactory.getIntegradorBoletos(cgs38.toBancoConfig())
    return integracao.emitirPDF(numRemessa)
  }
}