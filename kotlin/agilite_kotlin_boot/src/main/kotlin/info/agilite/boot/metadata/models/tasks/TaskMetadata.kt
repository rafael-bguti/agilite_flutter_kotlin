package info.agilite.boot.metadata.models.tasks

import info.agilite.boot.metadata.models.EntityMetadata

abstract class TaskMetadata (
  val taskName: String,
  val descr: String,
  val type: TaskTypeMetadata,
  val label: TaskLabelMetadata,
)

class TaskCrudMetadata(
  taskName: String,
  descr: String,
  label: TaskLabelMetadata,
  val entityMetadata: EntityMetadata,
  val idColumnName: String = "id",
) : TaskMetadata(taskName, descr, TaskTypeMetadata.crud, label) {
  fun copy(
    taskName: String = this.taskName,
    descr: String = this.descr,
    label: TaskLabelMetadata = this.label,
    entityMetadata: EntityMetadata = this.entityMetadata,
    idColumnName: String = this.idColumnName,
  ) = TaskCrudMetadata(taskName, descr, label, entityMetadata, idColumnName)
}

class TaskProcessMetadata(
  taskName: String,
  descr: String,
  label: TaskLabelMetadata,
) : TaskMetadata(taskName, descr, TaskTypeMetadata.process, label)


class TaskReportMetadata(
  taskName: String,
  descr: String,
  label: TaskLabelMetadata,
) : TaskMetadata(taskName, descr, TaskTypeMetadata.report, label)