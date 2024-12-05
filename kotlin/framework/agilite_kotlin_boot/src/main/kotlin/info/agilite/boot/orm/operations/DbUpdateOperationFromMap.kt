package info.agilite.boot.orm.operations

import info.agilite.boot.jdbcDialect
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.cache.DefaultEntityCache
import info.agilite.boot.orm.repositories.RootRepository

internal class DbUpdateOperationFromMap(
  private val tableName: String,
  private val values: Map<String, Any?>,
  private val schema: String? = null,
  private val outMapOfUidToCascade: MutableMap<Long, Long>? = null,
) : DBChangeOperation<Int> {
  override fun execute(repository: RootRepository): Int {
    val localValues = LowerCaseMap.of(values)

    val idName = "${tableName.lowercase()}id"
    val idValue = (values[idName] as Number?)?.toLong()
      ?: throw Exception("Id não informado o ID da entidade $tableName")

    val oneToMany = findOneToManyByTableName(tableName)
    val fields = localValues.keys.filter {
      it != idName && it.startsWith(tableName, true) && !oneToMany.containsKey(it)
    }.joinToString { "$it = :$it" }
    val params = jdbcDialect.parseParamsToQuery(tableName, localValues)
    params["id"] = idValue

    val localSchema = schema?.let { "$it." } ?: ""
    val query = "UPDATE $localSchema$tableName SET $fields WHERE $idName = :id"

    val updatedCount = repository.execute(query, params)
    DefaultEntityCache.invalidateById(tableName, idValue)
    if(oneToMany.isNotEmpty()){
      processOneToManyUpdateCascade(oneToMany, localValues, repository, schema, idValue, true, outMapOfUidToCascade)
    }

    return updatedCount
  }
}