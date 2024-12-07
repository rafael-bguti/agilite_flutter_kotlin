package info.agilite.boot.orm.operations

import info.agilite.core.json.JsonUtils
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository
import java.util.concurrent.atomic.AtomicLong

internal class DbUpdateOperationFromEntity(
  private val entity: Any,
) : DBChangeOperation<Int> {
  override fun execute(repository: RootRepository): Int {
    val tableNameAnsSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
    val tableName = tableNameAnsSchemaName.table
    val schema = tableNameAnsSchemaName.defaultSchema

    if(entity is AbstractEntity) entity.createUidsToCascadeOrm(AtomicLong(1L))
    val mapOfUidToCascade: MutableMap<Long, Long> = mutableMapOf()

    val values = LowerCaseMap.of(JsonUtils.toMapWithNull(entity))

    return DbUpdateOperationFromMap(tableName, values, schema, mapOfUidToCascade).execute(repository).also {
      if(entity is AbstractEntity) {
        entity.setIdsByUidToCascadeOrm(mapOfUidToCascade)
        entity.dbSaved()
      }
    }
  }
}