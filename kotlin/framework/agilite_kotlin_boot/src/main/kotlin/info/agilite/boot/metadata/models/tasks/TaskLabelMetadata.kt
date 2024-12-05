package info.agilite.boot.metadata.models.tasks

data class TaskLabelMetadata (
  val singular: String = "",
  val plural: String = "${singular}s",
)
