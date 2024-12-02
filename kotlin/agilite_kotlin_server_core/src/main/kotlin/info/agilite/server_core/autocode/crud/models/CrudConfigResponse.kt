package info.agilite.server_core.autocode.crud.models

import info.agilite.server_core.metadata.models.tasks.TaskCrudMetadata
import info.agilite.server_core.metadata.models.tasks.TaskLabelMetadata

data class CrudConfigResponse(
  val taskName: String,
  val descr: String,
  val label: TaskLabelMetadata,
){
  constructor(taskMetadata: TaskCrudMetadata) : this(
    taskName = taskMetadata.taskName,
    descr = taskMetadata.descr,
    label = taskMetadata.label
  )
}