package info.agilite.boot.metadata.data

import info.agilite.boot.metadata.models.AutocompleteConfig
import info.agilite.boot.metadata.models.EntityMetadata
import kotlin.reflect.KClass

interface MetadataDatasource {
  fun getEntityClass(entityName: String): KClass<*>?
  fun getEntity(entityName: String): EntityMetadata?
  fun getAutocompleteConfigByFieldName(autocompleteName: String): AutocompleteConfig?
}
