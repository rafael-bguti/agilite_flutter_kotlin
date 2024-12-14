package info.agilite.boot.orm.operations

import info.agilite.core.json.JsonUtils
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository
import java.util.concurrent.atomic.AtomicLong

internal class DbUpdateChangesOperationFromEntity(
  private val entity: AbstractEntity,
) : DBChangeOperation<Int> {
  override fun execute(repository: RootRepository): Int {
    val tableNameAndSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
    val tableName = tableNameAndSchemaName.table
    val schema = tableNameAndSchemaName.defaultSchema

    val values = entity.extractMapOfChagedProperties()

    entity.createUidsToCascadeOrm(AtomicLong(1L))
    val mapOfUidToCascade: MutableMap<Long, Long> = mutableMapOf()

    return DbUpdateOperationFromMap(tableName, values, schema, mapOfUidToCascade).execute(repository).also {
      entity.setIdsByUidToCascadeOrm(mapOfUidToCascade)
      entity.dbSaved()
    }
  }
}