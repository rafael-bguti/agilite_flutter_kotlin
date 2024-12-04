package info.agilite.boot.orm.jdbc

import info.agilite.boot.orm.dialects.PostgreSQLUtils
import java.sql.ResultSet


fun ResultSet.getORMTypedObject(column: Int): Any? {
  val obj: Any? = this.getObject(column)
  return PostgreSQLUtils.object2ORMObject(obj)
}

