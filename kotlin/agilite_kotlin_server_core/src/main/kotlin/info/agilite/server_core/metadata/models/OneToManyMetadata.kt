package info.agilite.server_core.metadata.models

data class OneToManyMetadata (
  val attIndex: Int,
  val childJoinClass: Class<*>,
  val childJoinColumn: String,
)