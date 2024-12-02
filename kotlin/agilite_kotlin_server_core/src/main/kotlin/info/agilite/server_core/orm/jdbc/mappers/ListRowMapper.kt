package info.agilite.server_core.orm.jdbc.mappers

import info.agilite.server_core.orm.jdbc.getORMTypedObject
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ListRowMapper : RowMapper<List<Any?>> {
  override fun mapRow(rs: ResultSet, rowNum: Int): List<Any?> {
    val result = mutableListOf<Any?>()
    val metadata = rs.metaData
    for (i in 1..metadata.columnCount) {
      result.add(rs.getORMTypedObject(i))
    }
    return result
  }
}