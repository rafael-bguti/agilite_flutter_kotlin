package info.agilite.boot.sdui

import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.ZipUtils

enum class SduiRequestType {
  CRUD,
}

class SduiRequest(
  val taskName: String,
  val providerClassName: String?,
  val type: SduiRequestType = SduiRequestType.CRUD,
) {
  fun toBase64(): String {
    return ZipUtils.compressToUrlBase64(JsonUtils.toJson(this))
  }

  companion object {
    fun fromBase64(base64: String): SduiRequest {
      return JsonUtils.fromJson(ZipUtils.decompressFromUrlBase64(base64), SduiRequest::class.java)
    }
  }
}