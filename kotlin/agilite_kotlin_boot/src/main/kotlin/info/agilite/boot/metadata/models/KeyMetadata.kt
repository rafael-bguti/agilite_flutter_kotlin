package info.agilite.boot.metadata.models

enum class KeyType {
  pk,
  mk,
  uk,
}

class KeyMetadata (
  val name: String,
  val type: KeyType,
  val fields: String,
)