package info.agilite.server_core.orm.jdbc.mappers

import info.agilite.shared.extensions.nest
import info.agilite.server_core.orm.jdbc.getORMTypedObject
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.JdbcUtils
import java.sql.ResultSet

class MapRowMapper(
  private val nested: Boolean = true,
  private val onlyNonNull: Boolean = true,
) : RowMapper<MutableMap<String, Any?>>{

  override fun mapRow(rs: ResultSet, rowNum: Int): MutableMap<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    val metadata = rs.metaData

    for (i in 1..metadata.columnCount) {
      val columnName = JdbcUtils.lookupColumnName(metadata, i)
      val value = rs.getORMTypedObject(i)
      if(onlyNonNull && value == null) continue

      result[columnName] = value
    }
    return if(nested) {
      val nestedResult = result.nest()
      if(!onlyNonNull){
        filterEmptyMaps(nestedResult)
      }
      nestedResult
    } else {
      result
    }
  }

  private fun filterEmptyMaps(map: MutableMap<String, Any?>) {
    val keys = map.keys.toList()
    keys.forEach {
      if(map[it] is Map<*, *>){
        val nestedMap = map[it] as MutableMap<String, Any?>
        if(mapIsAEmptyMap(nestedMap)){
          map[it] = null
        } else {
          filterEmptyMaps(nestedMap)
        }
      }
    }
  }

  private fun mapIsAEmptyMap(map: MutableMap<String, Any?>): Boolean {
    return map.isEmpty() || map.all { it.value == null }
  }

}