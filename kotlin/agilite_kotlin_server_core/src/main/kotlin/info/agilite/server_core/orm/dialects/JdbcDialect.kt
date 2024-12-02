package info.agilite.server_core.orm.dialects

import info.agilite.server_core.metadata.MetadataUtils
import info.agilite.server_core.defaultMetadataRepository
import info.agilite.server_core.metadata.exceptions.MetadataNotFoundException
import info.agilite.shared.model.LowerCaseMap

interface JdbcDialect {
  fun castToString(columnName: String): String
  fun coalesceString(columnName: String): String
  fun autogenerateId(): Boolean
  fun parseParamsToQuery(tableName: String, values: Map<String, Any?>): LowerCaseMap
  fun sequenceName(tableName: String): String
}

class PostgresDialect : JdbcDialect {
  override fun castToString(columnName: String): String {
    return "$columnName::text"
  }

  override fun coalesceString(columnName: String): String {
    return "COALESCE($columnName, '')"
  }

  override fun autogenerateId(): Boolean {
    return true
  }

  override fun parseParamsToQuery(tableName: String, values: Map<String, Any?>): LowerCaseMap {
    try {
      val result = LowerCaseMap()
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)

      entityMetadata.fields.forEach { field ->
        val value = values[field.name]
        result[field.name] = MetadataUtils.convertValueByField(field, value)
      }

      return result
    } catch (e: MetadataNotFoundException) {
      return LowerCaseMap.of(values)
    }
  }

  override fun sequenceName(tableName: String): String {
    val lowerName = tableName.lowercase()
    return "${lowerName}_${lowerName}id_seq"
  }
}