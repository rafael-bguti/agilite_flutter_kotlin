package info.agilite.boot.sdui.component

const val MOD_FONE = "fone"
const val MOD_CEP = "cep"
const val MOD_UF = "uf"
const val _MOD_NI = "ni";
const val MOD_NITIPO = "nitipo"

class SduiMetadataField (
  val name: String,
  val labelText: String? = null,
  val hintText: String? = null,
  val helperText: String? = null,
  val enabled: Boolean? = null,
  val mod: String? = null,
  id: String? = null,
) : SduiComponent(id)
