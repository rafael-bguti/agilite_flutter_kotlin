package info.agilite.boot.orm.operations

import info.agilite.boot.jdbcDialect
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.core.json.JsonUtils
import info.agilite.core.model.LowerCaseMap
import info.agilite.core.utils.ReflectionUtils
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder


class DbInsertOperationInBatch(
  private val entities: List<Any>,
) : DBChangeOperation<Long> {
  override fun execute(repository: RootRepository): Long {
    if(entities.isEmpty()) return 0

    val operationsByEntities = mutableMapOf<InsertOperationInBatch, List<ValuesToInsertInBatch>>()
    for(entity in entities){
      val tableNameAndSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
      val localValues = LowerCaseMap.of(JsonUtils.toMap(entity))
      val paramsMap = jdbcDialect.parseParamsToQuery(tableNameAndSchemaName.table, localValues)
      val idName = "${tableNameAndSchemaName.table.lowercase()}id"
      val autoGenerateId = paramsMap[idName] == null

      val valuesToInsert = ValuesToInsertInBatch(entity, paramsMap)
      val insertSQL = buildSimpleJdbcInsert(tableNameAndSchemaName.table, autoGenerateId, tableNameAndSchemaName.defaultSchema)
      InsertOperationInBatch(tableNameAndSchemaName.table, insertSQL, autoGenerateId).also {
        operationsByEntities[it] = operationsByEntities.getOrDefault(it, listOf()) + valuesToInsert
      }
    }

    operationsByEntities.forEach { (operation, valuesToInsert) ->
      val listLowerCaseMap = valuesToInsert.map { it.params }

      val keyHolder: KeyHolder = GeneratedKeyHolder()
      repository.jdbc.batchUpdate(
        operation.sql,
        SqlParameterSourceUtils.createBatch(listLowerCaseMap.toTypedArray()),
        keyHolder
      )

      if(operation.autoGenerateId){
        val ids = keyHolder.keyList.map { it["${operation.tableName.lowercase()}id"] as Number }
        for((index, valueToInsert) in valuesToInsert.withIndex()){
          ReflectionUtils.setIdValue(valueToInsert.entity, ids[index].toLong(), operation.tableName)
        }
      }
    }

    entities.forEach {
      if(it is AbstractEntity) {
        it.dbSaved(false)
      }
    }
    return entities.size.toLong()
  }
}

class ValuesToInsertInBatch (
  val entity: Any,
  val params: LowerCaseMap,
)

class InsertOperationInBatch(
  val tableName: String,
  val sql: String,
  val autoGenerateId: Boolean,
){
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as InsertOperationInBatch

    if (sql != other.sql) return false
    if (autoGenerateId != other.autoGenerateId) return false

    return true
  }
  override fun hashCode(): Int {
    var result = sql.hashCode()
    result = 31 * result + autoGenerateId.hashCode()
    return result
  }
}