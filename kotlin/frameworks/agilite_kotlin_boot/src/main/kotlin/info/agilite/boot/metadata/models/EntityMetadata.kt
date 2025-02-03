package info.agilite.boot.metadata.models

data class EntityMetadata (
  val name: String,
  val descr: String,
  val fields: List<FieldMetadata>,
  val keys: List<KeyMetadata>,
  val oneToMany: Map<String, OneToManyMetadata>,
) {
  fun fieldsToCrudList(): List<FieldMetadata> {
    return fields.filter { it.showInCrudList }
  }

  fun fieldsFilterable(): List<FieldMetadata>? {
    return fields.filter { it.filterable }.ifEmpty { null }
  }

  fun fieldsShowInFk(): List<FieldMetadata> {
    return fields.filter { it.showInFkAutoComplete } //TODO caso esteja vazio pegar os campos da UK
  }

  fun findKeysByType(type: KeyMetadataType): List<KeyMetadata> {
    return keys.filter { it.type == type } ?: emptyList()
  }

  //---- Contrato e Empresa
  fun getEmpresaField(): FieldMetadata? {
    return fields.firstOrNull { it.name.lowercase().equals("${name}empresa", true) }
  }

  fun entityHasEmpresaField(): Boolean{
    return getEmpresaField() != null
  }

  fun getOneToManyByName(name: String): OneToManyMetadata? {
    return oneToMany?.get(name.lowercase())
  }
}
