package info.agilite.server_core.metadata.models.tasks

data class TaskLabelMetadata (
  val singular: String = "",
  val plural: String = "${singular}s",
)
