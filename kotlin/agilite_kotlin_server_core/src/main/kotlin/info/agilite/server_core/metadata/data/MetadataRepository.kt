package info.agilite.server_core.metadata.data

import info.agilite.server_core.metadata.MetadataUtils
import info.agilite.server_core.metadata.exceptions.*
import info.agilite.server_core.metadata.models.*
import info.agilite.server_core.metadata.models.tasks.TaskMetadata
import kotlin.reflect.KClass

interface MetadataRepository {
  fun loadEntityClass(entityName: String): KClass<*>

  fun loadEntityMetadata(entityName: String): EntityMetadata
  fun loadEntityMetadataByFieldName(fieldName: String): EntityMetadata

  fun loadFieldMetadata(fieldName: String): FieldMetadata
  fun loadAutocompleteFieldMetadata(fieldName: String, taskName: String? = null): AutocompleteConfig
  fun <T : TaskMetadata> loadTaskMetadata(taskName: String, klass: KClass<T>): T
}


class MetadataRepositoryImpl(
  val dataSource: MetadataDatasource
) : MetadataRepository {
  val fieldMetadataCache = mutableMapOf<String, FieldMetadata>()

  override fun loadEntityClass(entityName: String): KClass<*> {
    return dataSource.getEntityClass(entityName) ?: throw EntityClassNotFoundException(entityName)
  }

  override fun loadEntityMetadata(entityName: String): EntityMetadata {
    return dataSource.getEntity(entityName.uppercase()) ?: throw EntityMetadataNotFoundException(entityName)
  }

  override fun loadEntityMetadataByFieldName(fieldName: String): EntityMetadata {
    MetadataUtils.extractEntityNameFromFieldName(fieldName).let { entityName ->
      return loadEntityMetadata(entityName)
    }
  }

  override fun loadFieldMetadata(fieldName: String): FieldMetadata {
    if (fieldMetadataCache.containsKey(fieldName)) {
      return fieldMetadataCache[fieldName]!!
    }

    MetadataUtils.extractEntityNameFromFieldName(fieldName).let { entityName ->
      return loadEntityMetadata(entityName).fields.find {
        it.name.equals(fieldName, true)
      }?.let {
        fieldMetadataCache[fieldName] = it
        it
      } ?: throw FieldMetadataNotFoundException(fieldName)
    }
  }

  override fun loadAutocompleteFieldMetadata(autocompleteName: String, taskName: String?): AutocompleteConfig {
    val nameWithTask = taskName?.let { "${it}_$autocompleteName" } ?: autocompleteName
    return dataSource.getAutocompleteConfigByFieldName(nameWithTask) ?:
      buildAutocompleteConfigByFieldName(autocompleteName) ?:
      throw AutocompleteMetadataNotFoundException(autocompleteName)
  }

  override fun <T : TaskMetadata> loadTaskMetadata(taskName: String, klass: KClass<T>): T {
    val metadata = dataSource.getTask(taskName.uppercase()) ?: throw TaskMetadataNotFoundException(taskName)
    if (klass.isInstance(metadata)) {
      return metadata as T
    } else {
      throw RuntimeException("Task $taskName is not a ${klass.simpleName}")
    }
  }

  private fun buildAutocompleteConfigByFieldName(fieldName: String): AutocompleteConfig? {
    val fieldMetadata = loadFieldMetadata(fieldName)
    if(fieldMetadata.type != FieldTypeMetadata.fk)return null

    val fkEntityName = MetadataUtils.extractEntityNameFromFieldName(fieldMetadata.foreignKeyEntity!!)
    val columns = getColumnsFkByEntityName(fkEntityName) ?: return null
    return AutocompleteConfig(
      field = fieldMetadata,
      table = fieldMetadata.foreignKeyEntity,
      columnsToSelect = columns,
    )
  }

  private fun getColumnsFkByEntityName(entityName: String): String? {
    val metadata = loadEntityMetadata(entityName)
    val fieldsToFK = metadata.fieldsShowInFk()
    if (fieldsToFK.isEmpty()){
      return null
    }
    return fieldsToFK.joinToString { it.name }
  }

}
