package info.agilite.server_core.metadata.data

import info.agilite.server_core.metadata.models.AutocompleteConfig
import info.agilite.server_core.metadata.models.EntityMetadata
import info.agilite.server_core.metadata.models.tasks.TaskMetadata
import kotlin.reflect.KClass

interface MetadataDatasource {
  fun getEntityClass(entityName: String): KClass<*>?

  fun getEntity(entityName: String): EntityMetadata?
  fun getTask(taskCode: String): TaskMetadata?

  fun getAutocompleteConfigByFieldName(autocompleteName: String): AutocompleteConfig?
}
