package info.agilite.server_core.orm.operations

import info.agilite.server_core.jdbcDialect
import info.agilite.shared.model.LowerCaseMap
import info.agilite.server_core.orm.UID_TO_ORM_KEY
import info.agilite.server_core.orm.repositories.RootRepository
import info.agilite.shared.utils.ReflectionUtils

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

    val jdbcInsert = buildSimpleJdbcInsert(repository, tableName, id == null, schema)
    if(id == null){
      id = jdbcInsert.executeAndReturnKey(params).toLong()
    }else{
      jdbcInsert.execute(params)
    }

    ReflectionUtils.setIdValue(values, id, tableName)
    if(oneToMany.isNotEmpty()){
      processOneToManyUpdateCascade(oneToMany, localValues, repository, schema, id, false, outMapOfUidToCascade)
    }

    localValues[UID_TO_ORM_KEY]?. let {
      outMapOfUidToCascade?.put(it as Long, id)
    }
    return id
  }
}