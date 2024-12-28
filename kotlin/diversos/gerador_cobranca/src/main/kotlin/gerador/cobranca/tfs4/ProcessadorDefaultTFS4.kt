package gerador.cobranca.tfs4

import gerador.cobranca.tfs4.GeradorValoresTFS4.preco


class ProcessadorDefaultTFS4() : Processador() {
  override fun processarValores(licenca: ConsumoTFS4Licenca) {
    processarNFeMDFe(licenca)
    processarCTe(licenca)
    processarESocialReinf(licenca)

    licenca.totalizar()
  }

  private fun processarNFeMDFe(licenca: ConsumoTFS4Licenca) {
    if(!licenca.jaProcessouAlgumDoc(setOf(SistemaDFe.NFe, SistemaDFe.MDFe)))return

    var contratosMultinfe = contratoMultiNFe[licenca.licencaCNPJ]
    if (contratosMultinfe?.nfe != null) {
      licenca.contratoMultiNFe = contratosMultinfe
      processarPeloContratoDoMultiNFe(licenca, contratosMultinfe.nfe!!, setOf(SistemaDFe.NFe, SistemaDFe.MDFe))
    } else {
      processar(licenca, setOf(SistemaDFe.NFe, SistemaDFe.MDFe))
    }
  }

  private fun processarCTe(licenca: ConsumoTFS4Licenca) {
    if(!licenca.jaProcessouAlgumDoc(setOf(SistemaDFe.CTe)))return

    var contratosMultinfe = contratoMultiNFe[licenca.licencaCNPJ]
    if (contratosMultinfe?.cte != null || contratosMultinfe?.nfe != null) {
      var valoresCteMultiNFe = criarContratoCTE(contratosMultinfe)
      if(licenca.qtdBySistemas(setOf(SistemaDFe.CTe)) == 0) return
      processarPeloContratoDoMultiNFe(licenca, valoresCteMultiNFe, setOf(SistemaDFe.CTe))
    } else {
      processar(licenca, setOf(SistemaDFe.CTe))
    }
  }

  private fun processarESocialReinf(licenca: ConsumoTFS4Licenca) {
    if(!licenca.jaProcessouAlgumDoc(setOf(SistemaDFe.ESocial, SistemaDFe.Reinf)))return

    if (contratosTFSam3.contains(licenca.licencaCNPJ)) {
      CobrancaESocialReinfTFS3().processar(licenca)
    } else {
      processar(licenca, setOf(SistemaDFe.ESocial))
      processar(licenca, setOf(SistemaDFe.Reinf))
    }
  }

  private fun processar(licenca: ConsumoTFS4Licenca, sistemas: Set<SistemaDFe>) {
    val qtdDocs = licenca.qtdBySistemas(sistemas)
    if(qtdDocs == 0) return

    licenca.historicoConsumo.add(
      HistoricoConsumo(
        licenca.montarHistorico( sistemas),
        preco(qtdDocs)
      )
    )
  }

  private fun processarPeloContratoDoMultiNFe(licenca: ConsumoTFS4Licenca, contrato: SitemaContratoMultiNFe, sistemas: Set<SistemaDFe>){
    val qtdDocs = licenca.qtdBySistemas(sistemas)
    var valorNFe = contrato.mensalidade
    if(qtdDocs > contrato.qtdDocsInclusoNaMensalidade){
      valorNFe += ((qtdDocs - contrato.qtdDocsInclusoNaMensalidade) * contrato.valorPorDocAdicional)
    }
    licenca.historicoConsumo.add(
      HistoricoConsumo(
        licenca.montarHistorico(sistemas) + ". Cobrança pelo contrato do MultiNFE-SAM3",
        valorNFe.toBigDecimal()
      )
    )
  }

  private fun criarContratoCTE(contratoMNFe: ContratoMultiNFe): SitemaContratoMultiNFe{
    var contrato = contratoMNFe.cte
    if (contrato == null) {
      contrato = SitemaContratoMultiNFe(
        0.0,
        0,
        contratoMNFe.nfe!!.valorPorDocAdicional,
      )
    }
    return contrato
  }

}