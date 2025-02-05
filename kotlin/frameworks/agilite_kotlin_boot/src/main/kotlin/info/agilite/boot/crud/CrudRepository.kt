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

  fun save(entity: Any)
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

  override fun save(entity: Any) {
    repository.insertOrUpdate(entity)
  }
}

enum class MoreFiltersType {
  BETWEEN,
  SINGLE,
}

data class DetailedFilterData(
  val fieldName: String,
  val type: MoreFiltersType,
  var value1: Any?,
  var value2: Any?
){
  fun getWhereClause(dialect: JdbcDialect): String {
    return when(type){
      MoreFiltersType.BETWEEN -> " $fieldName BETWEEN :$fieldName" + "Inicial AND :$fieldName" + "Final "
      else -> {
        if(value1 is String){
          value1 = "%$value1%"
          return " $fieldName ILIKE :$fieldName"
        }else{
          return " $fieldName = :$fieldName"
        }
      }
    }
  }

  fun getParameters(): Map<String, Any?> {
    return when(type){
      MoreFiltersType.BETWEEN -> mapOf("${fieldName}Inicial" to value1, "${fieldName}Final" to value2)
      else -> mapOf(fieldName to value1)
    }
  }
}


