package info.agilite.boot.crud

import info.agilite.boot.sdui.component.SduiComponent

data class CrudListResponse(
  val currentPage: Int,
  val pageSize: Int,

  val data: List<Map<String, Any?>>,

  var groups: List<CrudListGroup>? = null,
  var selectedGroupIndex: Int? = null,
)

data class CrudEditResponse(
  val data: Map<String, Any?>,
  val sduiForm: SduiComponent? = null,
  val editable: Boolean = true,
)
