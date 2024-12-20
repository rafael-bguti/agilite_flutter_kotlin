package gerador.cobranca.tfs4

import java.math.BigDecimal

data class ConsumoTFS4Revenda(
  val revendaCnpj: String,
  val revenda: String,
  val licencas: MutableList<ConsumoTFS4Licenca>,
)

data class ConsumoTFS4Licenca(
  val licencaNum: String,
  val licencaCNPJ: String,
  val licencaRs: String,

  val consumos: MutableList<Consumo>,

  var cnpjCobrancaMultinfe: String? = null,
  var processado: Boolean = false,
  val historicoConsumo: MutableList<HistoricoConsumo> = mutableListOf(),
) {
  fun jaProcessouAlgumDoc(sistemas: Set<SistemaDFe>): Boolean {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.any { it.possuiArmazenamentoAnterior }
  }
  fun qtdBySistemas(sistemas: Set<SistemaDFe>): Int {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.sumOf { it.qtd }
  }
  fun qtdEmpresas(sistemas: Set<SistemaDFe>): Int {
    return consumos.filter { sistemas.contains(it.sistemaDFe) }.map { it.empresaCNPJ }.distinct().size
  }
  fun qtdTotal(): Int {
    return consumos.sumOf { it.qtd }
  }
  fun montarHistorico(sistemas: Set<SistemaDFe>): String {
    val consumosPorSistema = consumos.filter { sistemas.contains(it.sistemaDFe) && it.qtd > 0 }
    val qtdDocs = consumosPorSistema.sumOf { it.qtd }
    val empresas = consumosPorSistema.map { "${it.empresaCNPJ}-${it.empresaNa} (${it.qtd})" }.joinToString("\n - ", prefix = " - ")

    return "Processamento de ${qtdDocs} documentos de ${sistemas.joinToString(", ") { it.name }}. Da(a) empresa(s): \n" +
      "$empresas"
  }
}

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