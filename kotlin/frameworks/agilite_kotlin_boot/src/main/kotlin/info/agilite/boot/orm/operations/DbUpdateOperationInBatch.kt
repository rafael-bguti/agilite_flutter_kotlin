package info.agilite.boot.orm.operations

import com.fasterxml.jackson.core.type.TypeReference
import info.agilite.core.json.JsonUtils
import info.agilite.boot.jdbcDialect
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils

class DbUpdateOperationInBatch(
  private val entities: List<Any>,
) : DBChangeOperation<Long> {
  override fun execute(repository: RootRepository): Long {
    if(entities.isEmpty()) return 0

    val tableNameAnsSchemaName = EntityMappingContext.getTableAndSchema(entities.first()::class.java)
    val listMaps = JsonUtils.convertValue(entities, object : TypeReference<List<Map<String, Any?>>>() {})
    val localValues = listMaps.map { jdbcDialect.parseParamsToQuery(tableNameAnsSchemaName.table, it) }

    val idName = "${tableNameAnsSchemaName.table.lowercase()}id"
    listMaps.find { it[idName] == null }?.let {
      throw Exception("Id não informado o ID da entidade ${tableNameAnsSchemaName.table}")
    }

    val oneToMany = findOneToManyByTableName(tableNameAnsSchemaName.table)
    val fields = localValues.first().keys.filter {
      it != idName && !oneToMany.containsKey(it)
    }.joinToString { "$it = :$it" }

    val localSchema = tableNameAnsSchemaName.defaultSchema?.let { "$it." } ?: ""
    val query = "UPDATE $localSchema${tableNameAnsSchemaName.table} SET $fields WHERE $idName = :$idName"

    println(localValues)

    val result = repository.jdbc.batchUpdate(
      query,
      SqlParameterSourceUtils.createBatch(localValues.toTypedArray())
    )

    entities.forEach {
      if(it is AbstractEntity) {
        it.dbSaved(false)
      }
    }

    return result.size.toLong()
  }
}