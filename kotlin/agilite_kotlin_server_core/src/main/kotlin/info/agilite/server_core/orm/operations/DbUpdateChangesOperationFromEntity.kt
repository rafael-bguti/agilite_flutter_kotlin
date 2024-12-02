package info.agilite.server_core.orm.operations

import info.agilite.shared.json.JsonUtils
import info.agilite.shared.model.LowerCaseMap
import info.agilite.server_core.orm.AbstractEntity
import info.agilite.server_core.orm.EntityMappingContext
import info.agilite.server_core.orm.repositories.RootRepository
import java.util.concurrent.atomic.AtomicLong

internal class DbUpdateChangesOperationFromEntity(
  private val entity: AbstractEntity,
) : DBChangeOperation<Int> {
  override fun execute(repository: RootRepository): Int {
    val tableNameAnsSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
    val tableName = tableNameAnsSchemaName.table
    val schema = tableNameAnsSchemaName.defaultSchema

    entity.createUidsToCascadeOrm(AtomicLong(1L))
    val mapOfUidToCascade: MutableMap<Long, Long> = mutableMapOf()

    val chagedPropertiesName = loadChangedPropertieNames(entity)
    val values = LowerCaseMap.of(JsonUtils.toMapWithNull(entity)).filterKeys {
      it.lowercase() == "${tableName}id".lowercase() || chagedPropertiesName.contains(it)
    }

    return DbUpdateOperationFromMap(tableName, values, schema, mapOfUidToCascade).execute(repository).also {
      entity.setIdsByUidToCascadeOrm(mapOfUidToCascade)
      entity.dbSaved()
    }
  }

  private fun loadChangedPropertieNames(entity: AbstractEntity): Set<String> {
    val metadata = entity.getMetadata()
    val setOfChangedProperties = entity.attChanged
    val result = mutableSetOf<String>()
    metadata.fields.forEach { field ->
      if (setOfChangedProperties.contains(field.attIndex)) {
        result.add(field.name.lowercase())
      }
    }

    metadata.oneToMany.entries.forEach { oneToMany ->
      if (setOfChangedProperties.contains(oneToMany.value.attIndex)) {
        result.add(oneToMany.key.lowercase())
      }
    }

    return result
  }
}