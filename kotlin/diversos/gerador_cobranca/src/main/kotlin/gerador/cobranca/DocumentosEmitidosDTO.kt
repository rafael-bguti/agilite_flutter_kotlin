package gerador.cobranca

import info.agilite.core.extensions.numbersOnly
import info.agilite.core.model.LowerCaseMap

data class DocumentosEmitidosDTO(
  val cnpj: String,
  val cnpjCob: String?,
  val rs: String,

  val contratoCnpj: String?,
  val contratoCnpjCob: String?,
  val contratoRs: String?,

  val sistema: SistemaConsumo,
  val qtdDocs: Long,
) {
  constructor(rsMultiNFe: LowerCaseMap, sistema: SistemaConsumo) : this(
    rsMultiNFe.getString("cnpj")!!.numbersOnly(),
    rsMultiNFe.getString("cnpjcob")?.numbersOnly(),
    rsMultiNFe["rs"] as String,

    rsMultiNFe.getString("cont_cnpj")?.numbersOnly(),
    rsMultiNFe.getString("cont_cnpjcob")?.numbersOnly(),
    rsMultiNFe["cont_rs"] as String?,

    sistema,
    rsMultiNFe.getLong("qtd")!!
  )
}

enum class SistemaConsumo {
  TFS3,
  TFS4,
  MULTINFE
}

