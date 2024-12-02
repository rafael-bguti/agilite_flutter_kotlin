
package info.agilite.server_core.orm.jdbc.mappers

import info.agilite.shared.json.JsonUtils
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class EntityDataClassRowMapper<T>(
  private val mappedClass: Class<T>,
) : RowMapper<T> {
  private val mapper = MapRowMapper(nested = true, onlyNonNull = false)

  override fun mapRow(rs: ResultSet, rowNum: Int): T {
    val nested = mapper.mapRow(rs, rowNum)
    val result = JsonUtils.fromMap(nested, mappedClass)

    return result
  }
}
