package info.agilite.boot.sdui.component

const val MOD_FONE = "fone"
const val MOD_CEP = "cep"
const val MOD_UF = "uf"
const val MOD_NI = "ni";
const val MOD_NITIPO = "niTipo"

//Apenas para colunas
const val MOD_SDUI = "sdui" //O retorno dentro dos dados será um Json com os dados do SDUI
const val MOD_BADGE = "badge" //O retorno dentro dos deve ser o TEXTO|COR (Hexadecimal)

private const val MOD_STATUS_PAGTO_BY_DATE_NAME = "statusPagtoByDate"
val MOD_STATUS_DATE_FUNCTION: (fieldDataVencimento: String, fieldDataPagamento: String) -> String = { fieldDataVencimento, fieldDataPagamento ->
  "$MOD_STATUS_PAGTO_BY_DATE_NAME:$fieldDataVencimento:$fieldDataPagamento"
}

class SduiMetadataField (
  val name: String,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
  val mod: String? = null,
  id: String? = null,
) : SduiComponent(id)
