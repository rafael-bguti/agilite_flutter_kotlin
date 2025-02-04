package info.agilite.boot.sdui.component

import org.apache.coyote.http11.Constants.a

const val MOD_FONE = "fone"
const val MOD_CEP = "cep"
const val MOD_UF = "uf"
const val MOD_NI = "ni";
const val MOD_NITIPO = "nitipo"

//Apenas para colunas
private const val MOD_STATUS_DATE_NAME = "statusDate"
val MOD_STATUS_DATE_FUNCTION: (fieldDataVencimento: String, fieldDataPagamento: String) -> String = { fieldDataVencimento, fieldDataPagamento ->
  "$MOD_STATUS_DATE_NAME|$fieldDataVencimento|$fieldDataPagamento"
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
