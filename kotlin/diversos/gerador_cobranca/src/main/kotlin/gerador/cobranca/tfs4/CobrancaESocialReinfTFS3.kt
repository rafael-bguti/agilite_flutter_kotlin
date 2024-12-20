package gerador.cobranca.tfs4

import java.math.BigDecimal


class CobrancaESocialReinfTFS3() {
  fun processar(licenca: ConsumoTFS4Licenca) {
    val limit1QtdEmpresas = 2
    val limit2QtdEmpresas = 5

    val limit1QtdEventos = 500
    val limit2Qtdeventos = 1500

    val qtdDocs = licenca.qtdBySistemas(setOf(SistemaDFe.ESocial, SistemaDFe.Reinf))
    val qtdEmpresas = licenca.qtdEmpresas(setOf(SistemaDFe.ESocial, SistemaDFe.Reinf))

    var pacotePorQtdEmpresas = if (qtdEmpresas <= limit1QtdEmpresas) {
      0
    } else if (qtdEmpresas <= limit2QtdEmpresas) {
      1
    } else {
      2
    }

    var pacotePorQtdEventos = if (qtdDocs <= limit1QtdEventos) {
      0
    } else if (qtdDocs <= limit2Qtdeventos) {
      1
    } else {
      2
    }

    val precos = arrayOf(
      BigDecimal("54.88"),
      BigDecimal("88.48"),
      BigDecimal("166.88")
    )
    val valor = precos[Math.max(pacotePorQtdEmpresas, pacotePorQtdEventos)]

    licenca.historicoConsumo.add(
      HistoricoConsumo(
        licenca.montarHistorico(setOf(SistemaDFe.ESocial, SistemaDFe.Reinf)) + ". Cobrança pelo contrato do TransmissorFiscal-SAM3" ,
        valor
      )
    )
  }
}
