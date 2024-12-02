package info.agilite.server_core.orm.operations

import info.agilite.shared.json.JsonUtils
import info.agilite.shared.model.LowerCaseMap
import info.agilite.server_core.orm.AbstractEntity
import info.agilite.server_core.orm.EntityMappingContext
import info.agilite.server_core.orm.repositories.RootRepository
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