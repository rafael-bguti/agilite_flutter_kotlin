package gerador.cobranca.tfs4

abstract class Processador {
  abstract fun processarValores(consumo: ConsumoTFS4Licenca)
}

class IgnorarCobranca : Processador() {
  override fun processarValores(consumo: ConsumoTFS4Licenca) {
    consumo.processado = true
  }
}

class ValorZeradoAte100DocsE1Empresa : Processador() {
  override fun processarValores(consumo: ConsumoTFS4Licenca) {
    val consumoTotal = consumo.consumos.sumOf { it.qtd }
    val qtdEmpresas = consumo.consumos.map { it.empresaCNPJ }.distinct().size

    if (consumoTotal > 100 || qtdEmpresas > 1) {
      println("ATENÇÃO: A empresa ${consumo.licencaCNPJ}-${consumo.licencaRs} ultrapassou o limite de 100 e 1 empresa para ter o valor ZERADO")
      ProcessadorDefaultTFS4().processarValores(consumo)
    } else {
      consumo.processado = true
    }
  }
}
