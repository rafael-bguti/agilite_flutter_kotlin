package info.agilite.boot.metadata.models

data class OneToManyMetadata (
  val attIndex: Int,
  val childJoinClass: Class<*>,
  val childJoinColumn: String,
)