package info.agilite.server_core.orm.dialects

import info.agilite.shared.json.JsonUtils
import info.agilite.shared.model.LowerCaseMap
import org.postgresql.util.PGobject
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

class PostgreSQLUtils {
  companion object {
    fun createLowerConcatLikeByColumns(columns: List<String>, divisor: String?, paramName: String): String {
      val separator = if(divisor != null) " || '$divisor' || " else " || "
      return " LOWER(" + columns.joinToString(separator) { it } + ") LIKE LOWER(:$paramName) "
    }

    fun convertToStringWithCoalesce(column: String): String {
      return "COALESCE(CAST($column as TEXT), '')"
    }

    fun object2ORMObject(obj: Any?): Any? {
      if (obj == null) return null
      if (obj is Double) return BigDecimal(obj.toString())
      if (obj is Date) return obj.toLocalDate()
      if (obj is Time) return obj.toLocalTime()
      if (obj is Timestamp) return obj.toLocalDateTime()
      if (obj is PGobject) JsonUtils.fromJson(obj.toString(), LowerCaseMap::class.java)

      return obj
    }

  }
}
