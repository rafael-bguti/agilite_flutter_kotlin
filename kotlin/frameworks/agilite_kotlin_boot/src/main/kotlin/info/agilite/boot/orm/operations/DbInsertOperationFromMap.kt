package info.agilite.boot.orm.operations

import info.agilite.boot.jdbcDialect
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.UID_TO_ORM_KEY
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.core.utils.ReflectionUtils

internal class DbInsertOperationFromMap(
  private val tableName: String,
  private val values: Map<String, Any?>,
  private val schema: String? = null,
  private val outMapOfUidToCascade: MutableMap<Long, Long>? = null,
) : DBChangeOperation<Long> {
  override fun execute(repository: RootRepository): Long {
    val localValues = LowerCaseMap.of(values)
    addEmpresaDefaultToMap(tableName, localValues)
    val params = jdbcDialect.parseParamsToQuery(tableName, localValues)

    val oneToMany = findOneToManyByTableName(tableName)

    val idName = "${tableName.lowercase()}id"
    var id: Long? = localValues.getLong(idName)

    val jdbcInsert = buildSimpleJdbcInsert(tableName, id == null, schema)
    if(id == null){
      id = repository.uniqueSingleColumn(Long::class, jdbcInsert, params)
    }else{
      repository.execute(jdbcInsert, params)
    }

    ReflectionUtils.setIdValue(values, id!!, tableName)
    if(oneToMany.isNotEmpty()){
      processOneToManyUpdateCascade(oneToMany, localValues, repository, schema, id, false, outMapOfUidToCascade)
    }

    localValues[UID_TO_ORM_KEY]?. let {
      outMapOfUidToCascade?.put(it as Long, id)
    }
    return id
  }
}