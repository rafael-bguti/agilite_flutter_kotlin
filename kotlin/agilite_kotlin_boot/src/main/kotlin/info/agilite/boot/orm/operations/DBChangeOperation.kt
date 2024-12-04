package info.agilite.boot.orm.operations

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.exceptions.MetadataNotFoundException
import info.agilite.boot.metadata.models.OneToManyMetadata
import info.agilite.core.model.LowerCaseMap
import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.core.utils.ReflectionUtils
import org.springframework.jdbc.core.simple.SimpleJdbcInsert


internal interface DBChangeOperation<T> {
  fun execute(repository: RootRepository): T

  fun findOneToManyByTableName(tableName: String): Map<String, OneToManyMetadata> {
    return defaultMetadataRepository.loadEntityMetadata(tableName).oneToMany
  }

  fun processOneToManyUpdateCascade(
    oneToMany: Map<String, OneToManyMetadata>,
    localValues: LowerCaseMap,
    repository: RootRepository,
    schema: String? = null,
    parentId: Long? = null,
    deleteOrphans: Boolean,
    outMapOfUidToCascade: MutableMap<Long, Long>? = null,
  ) {
    for (childKey in oneToMany.keys) {
      val listOfChildren = localValues[childKey] ?: continue
      if (listOfChildren !is Collection<*>) throw Exception("Valor do OneToMany '$childKey' deve ser um Collection")
      val oneToManyMetadata = oneToMany[childKey]!!
      val childIds = mutableListOf<Long>()

      listOfChildren.onEach {
        var values = it as Map<String, Any?>
        if(parentId != null && values[oneToManyMetadata.childJoinColumn] == null) {
          values = values.toMutableMap()
          values[oneToManyMetadata.childJoinColumn] = parentId
        }

        val tableName = oneToManyMetadata.childJoinClass.simpleName
        var idValue = ReflectionUtils.getIdValue(values, oneToManyMetadata.childJoinClass.simpleName)
        if(idValue == null){
          idValue = DbInsertOperationFromMap(tableName, values, schema, outMapOfUidToCascade).execute(repository)
        }else {
          DbUpdateOperationFromMap(tableName, values, schema, outMapOfUidToCascade).execute(repository)
        }
        childIds.add(idValue)
      }

      if(deleteOrphans){
        val sql = "DELETE FROM ${oneToManyMetadata.childJoinClass.simpleName} WHERE ${oneToManyMetadata.childJoinColumn} = :parentId AND ${oneToManyMetadata.childJoinClass.simpleName.lowercase()}id NOT IN (:childIds)"
        repository.execute(sql, mapOf("parentId" to parentId, "childIds" to childIds))
      }
    }
  }

  fun addEmpresaDefaultToMap(tableName: String, localValues: LowerCaseMap) {
    try {
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      if (entityMetadata.entityHasEmpresaField()) {
        val fieldEmpName = "${entityMetadata.name.lowercase()}empresa"
        if (!localValues.containsKey(fieldEmpName)) {
          localValues[fieldEmpName] = AgiliteWhere.getEmpresaId(entityMetadata)
        }
      }
    } catch (ignore: MetadataNotFoundException) {
      // Do nothing
    }
  }

  fun buildSimpleJdbcInsert(repository: RootRepository, tableName: String, useGenerateId: Boolean, schema: String?): SimpleJdbcInsert {
    return SimpleJdbcInsert(repository.jdbcTemplate).let {
      if(schema != null) {
        it.withSchemaName(schema)
      }
      if(useGenerateId){
        it.usingGeneratedKeyColumns("${tableName.lowercase()}id")
      }
      it.withTableName(tableName)

      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      val startIndex = if(useGenerateId) 1 else 0
      val columnNames = entityMetadata.fields.subList(startIndex, entityMetadata.fields.size).map { it.name }
      it.usingColumns(*columnNames.toTypedArray())
    }
  }
}
