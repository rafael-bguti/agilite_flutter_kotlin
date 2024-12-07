package info.agilite.boot.metadata.models

enum class KeyMetadataType {
  pk,
  mk,
  uk,
}

class KeyMetadata (
  val name: String,
  val type: KeyMetadataType,
  val fields: String,
)