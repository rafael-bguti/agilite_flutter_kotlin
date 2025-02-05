package info.agilite.boot.sdui.component

import info.agilite.boot.metadata.models.tasks.TaskDescr

class SduiCrud(
  var taskName: String,
  var descr: TaskDescr,
  var columns: List<SduiColumn>,
  var customFilters: List<SduiComponent>? = null,
  var formBody: SduiComponent? = null,
  var moreFiltersWidget: SduiComponent? = null,
  var metadataToLoad: String? = null,
) : SduiComponent()