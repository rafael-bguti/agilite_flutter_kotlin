package info.agilite.boot.crud

import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.orm.dialects.JdbcDialect
import info.agilite.boot.orm.jdbc.mappers.MapRowMapper
import info.agilite.boot.orm.query.DbQuery
import info.agilite.boot.orm.repositories.DefaultRepository
import org.springframework.stereotype.Repository

interface AgiliteCrudRepository {
  fun findListData(query: DbQuery<*>): List<MutableMap<String, Any?>>
  fun findEditData(query: DbQuery<*>):Map<String, Any?>?

  fun insert(entity: Any)
  fun update(entity: Any) : Int
  fun delete(entityMetadata: EntityMetadata, ids: List<Long>)
}

@Repository
class DefaultCrudRepository(
  private val repository: DefaultRepository
): AgiliteCrudRepository {
  override fun findListData(query: DbQuery<*>): List<MutableMap<String, Any?>> {
    return repository.listMap(query, MapRowMapper(nested=false))
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


