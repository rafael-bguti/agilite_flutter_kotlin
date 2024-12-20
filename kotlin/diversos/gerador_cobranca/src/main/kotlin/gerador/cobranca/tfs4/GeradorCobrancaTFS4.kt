package gerador.cobranca.tfs4

import gerador.cobranca.NATUREZA_MNFE
import gerador.cobranca.NATUREZA_TFS3
import gerador.cobranca.faturamentos
import gerador.cobranca.model.Faturamento
import info.agilite.integradores.dtos.Cobranca

private lateinit var contratosMultiNFe: Map<String, ContratosMultinfe>
private lateinit var contratosTFSam3: Set<String>

//Processadores customizados
val processadoresCustomizadosPorCliente = mapOf<String, Processador>(
  // Multitec Sistemas
  "67919092000189" to IgnorarCobranca(),
  "64856974000154" to ValorZeradoAte100DocsE1Empresa(),
  "16937595000146" to ValorZeradoAte100DocsE1Empresa(),
)

class GeradorCobrancaTFS4 {
  fun gerar(consumoTFS4Revendas: List<ConsumoTFS4Revenda>): List<Faturamento> {
    carregarDadosIniciais(consumoTFS4Revendas)

    val consumoDasLicencas = consumoTFS4Revendas.map { rev -> rev.licencas }.flatten()
    for(consumoDaLicenca in consumoDasLicencas) {
      val processador = processadoresCustomizadosPorCliente[consumoDaLicenca.licencaCNPJ]

    }



    return emptyList()
  }

  private fun gerarCobrancaAPartirDoContratoDoMultiNFe(consumoPorLicenca: ConsumoTFS4Licenca, contratosMultinfe: ContratosMultinfe

  }

  private fun findCobrancaMNFeTFS3ByCnpjs(cnpjLicencaSAM4: String, cnpjContratoMultiNFe: String): List<Cobranca>{
    return faturamentos
      .filter {
        (it.cobranca.cliente.cnpj == cnpjContratoMultiNFe || it.cobranca.cliente.cnpj == cnpjLicencaSAM4) &&
        (it.cobranca.natureza == NATUREZA_MNFE || it.cobranca.natureza == NATUREZA_TFS3)
      }
      .map { it.cobranca }
  }

  private fun carregarDadosIniciais(consumoTFS4: List<ConsumoTFS4Revenda>) {
    val cnpjsLicencas = consumoTFS4.map { rev -> rev.licencas.map { it.licencaCNPJ } }.flatten()
    println("Buscando contratos no MultiNFe")
    contratosMultiNFe = MultiNFeRepository().localizarContratosMultiNFe(cnpjsLicencas)
    println("Buscando contratos no TFS3")
    contratosTFSam3 = TfSam3Repository().localizarContratosTFSam3(cnpjsLicencas)
    definirCnpjMultiNFe(consumoTFS4)
  }

  private fun definirCnpjMultiNFe(consumoTFS4: List<ConsumoTFS4Revenda>){
    consumoTFS4.forEach { revenda ->
      revenda.licencas.forEach { licenca ->
        val contrato = contratosMultiNFe[licenca.licencaCNPJ]
        if(contrato != null){
          licenca.cnpjCobrancaMultinfe = contrato.cnpjCobranca
        }
      }
    }

  }


}