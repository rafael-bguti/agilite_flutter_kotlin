package info.agilite.integradores.bancos.inter

import com.fasterxml.jackson.annotation.JsonProperty

data class BancoInterError(
  val title: String,
  val detail: String? = null,
  val violacoes: List<Violation>? = null
) {
  override fun toString(): String {
    return "Titulo: '$title'\nMais detalhes: $detail\nViolações:\n$violacoes"
  }
}

data class Violation(
  val razao: String?,
  val propriedade: String?,
  val valor: String?,
){
  override fun toString(): String {
    return " - Violação: '$razao'\nPropriedade: '$propriedade'\nValor: '$valor'\n"
  }
}