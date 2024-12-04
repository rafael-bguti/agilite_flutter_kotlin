package info.agilite.boot.metadata.data

import info.agilite.boot.metadata.models.AutocompleteConfig
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.tasks.TaskMetadata
import kotlin.reflect.KClass

interface MetadataDatasource {
  fun getEntityClass(entityName: String): KClass<*>?

  fun getEntity(entityName: String): EntityMetadata?
  fun getTask(taskCode: String): TaskMetadata?

  fun getAutocompleteConfigByFieldName(autocompleteName: String): AutocompleteConfig?
}
