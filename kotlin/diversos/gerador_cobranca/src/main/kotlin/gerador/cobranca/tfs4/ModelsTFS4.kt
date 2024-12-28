package gerador.cobranca.tfs4

import java.math.BigDecimal

data class ConsumoTFS4Licenca(
  val licencaNum: String,
  val licencaCNPJ: String,
  val licencaRs: String,
  val revenda: Revenda,
  val cobrarDiretamenteDoCliente: Boolean = false,
  val cnpjCobrancaSAM4DiretoCliente: String? = null,

  val consumos: MutableList<Consumo>,
  var total: BigDecimal = BigDecimal.ZERO,

  var cnpjCobrancaMultinfe: String? = null,
  val historicoConsumo: MutableList<HistoricoConsumo> = mutableListOf(),

  var contratoMultiNFe: ContratoMultiNFe? = null,
  var faturado: Boolean = false,
) {
  fun jaProcessouAlgumDoc(sistemas: Set<SistemaDFe>): Boolean {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.any { it.possuiArmazenamentoAnterior }
  }
  fun jaProcessouAlgumDoc(): Boolean {
    return consumos.any { it.possuiArmazenamentoAnterior }
  }
  fun qtdBySistemas(sistemas: Set<SistemaDFe>): Int {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.sumOf { it.qtd }
  }
  fun qtdEmpresas(sistemas: Set<SistemaDFe>): Int {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.map { it.empresaCNPJ }.distinct().size
  }
  fun totalizar() {
    total = historicoConsumo.sumOf { it.valor }
  }
  fun qtdTotalDocs(): Int {
    return consumos.sumOf { it.qtd }
  }
  fun getHistorico(): String {
    return historicoConsumo.joinToString("\n") { it.historico }
  }


  fun montarHistorico(sistemas: Set<SistemaDFe>): String {
    val consumosPorSistema = consumos.filter { sistemas.contains(it.sistemaDFe) && it.qtd > 0 }
    val qtdDocs = consumosPorSistema.sumOf { it.qtd }
    val empresas = consumosPorSistema.map { "${it.empresaCNPJ}-${it.empresaNa} (${it.qtd})" }.joinToString("\n - ", prefix = " - ")

    return "Processamento de ${qtdDocs} documentos de ${sistemas.joinToString(", ") { it.name }}. Da(a) empresa(s): \n" +
      "$empresas"
  }
}

data class Revenda(
  val revendaCnpj: String,
  val revendaRs: String,
)

data class HistoricoConsumo (
  val historico: String,
  val valor: BigDecimal,
)

data class Consumo(
  val empresaCNPJ: String,
  val empresaNa: String,
  val sistemaDFe: SistemaDFe,
  val qtd: Int,
  val possuiArmazenamentoAnterior: Boolean,
)