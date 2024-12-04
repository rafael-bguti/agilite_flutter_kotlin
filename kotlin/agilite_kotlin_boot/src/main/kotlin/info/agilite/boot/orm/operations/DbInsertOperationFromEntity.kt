package info.agilite.boot.orm.operations

import info.agilite.core.json.JsonUtils
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.core.utils.ReflectionUtils
import java.util.concurrent.atomic.AtomicLong

internal class DbInsertOperationFromEntity(
  private val entity: Any,
) : DBChangeOperation<Void?> {
  override fun execute(repository: RootRepository): Void? {
    val tableNameAnsSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
    val tableName = tableNameAnsSchemaName.table
    val schema = tableNameAnsSchemaName.defaultSchema

    if(entity is AbstractEntity) entity.createUidsToCascadeOrm(AtomicLong(1L))
    val mapOfUidToCascade: MutableMap<Long, Long> = mutableMapOf()

    val values = LowerCaseMap.of(JsonUtils.toMap(entity))
    val id = DbInsertOperationFromMap(tableName, values, schema, mapOfUidToCascade).execute(repository)

    ReflectionUtils.setIdValue(entity, id, tableName).also {
      if(entity is AbstractEntity) {
        entity.setIdsByUidToCascadeOrm(mapOfUidToCascade)
        entity.dbSaved()
      }
    }

    return null
  }


}