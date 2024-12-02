package info.agilite.server_core.autocode.crud

import info.agilite.server_core.autocode.crud.models.CrudListData
import info.agilite.server_core.metadata.models.EntityMetadata
import info.agilite.server_core.orm.dialects.JdbcDialect
import info.agilite.server_core.orm.jdbc.mappers.MapRowMapper
import info.agilite.server_core.orm.query.DbQuery
import info.agilite.server_core.orm.repositories.DefaultRepository
import org.springframework.stereotype.Repository

interface AgiliteCrudRepository {
  fun findListData(query: DbQuery<*>): CrudListData
  fun findEditData(query: DbQuery<*>):Map<String, Any?>?

  fun insert(entity: Any)
  fun update(entity: Any) : Int
  fun delete(entityMetadata: EntityMetadata, ids: List<Long>)
}

@Repository
class DefaultCrudRepository(
  private val repository: DefaultRepository
): AgiliteCrudRepository{
  override fun findListData(query: DbQuery<*>): CrudListData {
    val queryCount = query.copy(
      columnsProcessor = { " count(*) as count " },
      orderBy = { "" },
      limitQuery = null
    )
    val count = repository.uniqueSingleColumn(Long::class, queryCount.sql(), queryCount.getParams())
    val data = repository.listMap(query, MapRowMapper(nested=false))

    return CrudListData(0, count ?: 0L, data)
  }

  override fun delete(entityMetadata: EntityMetadata, ids: List<Long>) {
    val sql = "DELETE FROM ${entityMetadata.name} WHERE ${entityMetadata.name.lowercase()}id IN (:ids)"
    repository.execute(sql, mapOf("ids" to ids))
  }

  override fun findEditData(query: DbQuery<*>): Map<String, Any?>? {
    return repository.listMap(query).firstOrNull()
  }

  override fun insert(entity: Any) {
    repository.insert(entity)
  }

  override fun update(entity: Any): Int {
    return repository.update(entity)
  }
}

data class DetailedFilterData(
  val fieldName: String,
  val operator: String,
  var value1: Any?,
  var value2: Any?
){
  fun getWhereClause(dialect: JdbcDialect): String {
    return when(operator){
      "BETWEEN" -> "${dialect.coalesceString(dialect.castToString(fieldName))} BETWEEN :$fieldName" + "Inicial AND :$fieldName" + "Final"
      else -> "${dialect.coalesceString(dialect.castToString(fieldName))} $operator :$fieldName"
    }
  }

  fun getParameters(): Map<String, Any?> {
    return when(operator){
      "BETWEEN" -> mapOf("${fieldName}Inicial" to value1, "${fieldName}Final" to value2)
      else -> mapOf(fieldName to value1)
    }
  }
}


