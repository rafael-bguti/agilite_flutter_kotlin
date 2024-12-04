package info.agilite.boot.orm.operations

import com.fasterxml.jackson.core.type.TypeReference
import info.agilite.core.json.JsonUtils
import info.agilite.boot.jdbcDialect
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository

class DbInsertOperationInBatch(
  private val entities: List<Any>,
) : DBChangeOperation<Long> {
  override fun execute(repository: RootRepository): Long {
    if(entities.isEmpty()) return 0

    val tableNameAndSchemaName = EntityMappingContext.getTableAndSchema(entities.first()::class.java)
    val listMaps = JsonUtils.convertValue(entities, object : TypeReference<List<Map<String, Any?>>>() {})

    val idName = "${tableNameAndSchemaName.table.lowercase()}id"
    val containIdInMap = listMaps.first()[idName] != null

    val localValues = listMaps.map { jdbcDialect.parseParamsToQuery(tableNameAndSchemaName.table, it) }
    localValues.forEach { addEmpresaDefaultToMap(tableNameAndSchemaName.table, it) }

    val jdbcInsert = buildSimpleJdbcInsert(repository, tableNameAndSchemaName.table, !containIdInMap, tableNameAndSchemaName.defaultSchema)
    if(!containIdInMap){
      jdbcInsert.usingGeneratedKeyColumns(idName)
    }
    val result = jdbcInsert.executeBatch(*localValues.toTypedArray())

    entities.forEach {
      if(it is AbstractEntity) {
        it.dbSaved(false)
      }
    }
    return result.size.toLong()
  }
}