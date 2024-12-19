package info.agilite.boot.orm.jdbc.mappers

import info.agilite.core.extensions.nest
import info.agilite.boot.orm.jdbc.getORMTypedObject
import info.agilite.core.model.LowerCaseMap
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.JdbcUtils
import java.sql.ResultSet

class LowercaseMapRowMapper(
  private val nested: Boolean = false,
  private val onlyNonNull: Boolean = true,
) : RowMapper<LowerCaseMap>{

  override fun mapRow(rs: ResultSet, rowNum: Int): LowerCaseMap {
    val result = LowerCaseMap()
    val metadata = rs.metaData

    for (i in 1..metadata.columnCount) {
      val columnName = JdbcUtils.lookupColumnName(metadata, i)
      val value = rs.getORMTypedObject(i)
      if(onlyNonNull && value == null) continue

      result[columnName] = value
    }
    return if(nested) {
      val nestedResult = result.nestMap()
      if(!onlyNonNull){
        filterEmptyMaps(nestedResult)
      }
      nestedResult
    } else {
      result
    }
  }

  private fun filterEmptyMaps(map: LowerCaseMap) {
    val keys = map.keys.toList()
    keys.forEach {
      if(map[it] is LowerCaseMap){
        val nestedMap = map[it] as LowerCaseMap
        if(mapIsAEmptyMap(nestedMap)){
          map[it] = null
        } else {
          filterEmptyMaps(nestedMap)
        }
      }
    }
  }

  private fun mapIsAEmptyMap(map: LowerCaseMap): Boolean {
    return map.isEmpty() || map.all { it.value == null }
  }

}