package info.agilite.boot.autocode.crud.models

import info.agilite.boot.metadata.models.tasks.TaskCrudMetadata
import info.agilite.boot.metadata.models.tasks.TaskLabelMetadata

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