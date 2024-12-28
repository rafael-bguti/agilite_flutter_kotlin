package gerador.cobranca.daos

import gerador.cobranca.daos.DAO.cnnMNFe
import gerador.cobranca.daos.DAO.cnnTFS3
import info.agilite.core.model.LowerCaseMap
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object DAO {
  lateinit var cnnTFS4: Connection
  lateinit var cnnTFS3: Connection
  lateinit var cnnMNFe: Connection


  fun queryTFS4(sql: String, params: Map<String, Any?> = emptyMap()): List<LowerCaseMap> {
    return query(createCnnTFS4(), sql, params)
  }

  fun queryMultiNFe(sql: String, params: Map<String, Any?> = emptyMap()): List<LowerCaseMap> {
    return query(createCnnMultinfe(), sql, params)
  }

  fun queryTFS3(sql: String, params: Map<String, Any?> = emptyMap()): List<LowerCaseMap> {
    return query(createCnnTFS3(), sql, params)
  }

  private fun query(cnn: Connection, sql: String, params: Map<String, Any?>): List<LowerCaseMap> {
    return cnn.prepareStatement(sql).use {
      if (params.isNotEmpty()) {
        var idx = 1
        for (p in params) {
          it.setObject(idx++, p)
        }
      }

      convertResultSet(it.executeQuery())
    }
  }


  private fun convertResultSet(rs: ResultSet): List<LowerCaseMap> {
    val listMap: MutableList<LowerCaseMap> = ArrayList()
    val metaData = rs.metaData
    while (rs.next()) {
      val row = LowerCaseMap()
      for (i in 1..metaData.columnCount) {
        row[metaData.getColumnName(i)] = rs.getObject(i)
      }
      listMap.add(row)
    }

    return listMap
  }

  private fun createCnnMultinfe(): Connection {
    if (!::cnnMNFe.isInitialized) {
      Class.forName("org.postgresql.Driver")
      cnnMNFe = DriverManager.getConnection(
        "jdbc:postgresql://34.198.165.190/multinfe",
        "postgres",
        "Gi5B28i.Ob{5ZxIwjzLaY\$"
      )
    }
    return cnnMNFe
  }

  private fun createCnnTFS4(): Connection {
    if (!::cnnTFS4.isInitialized) {
      Class.forName("org.postgresql.Driver")
      cnnTFS4 = DriverManager.getConnection(
        "jdbc:postgresql://34.198.165.190/tfs4",
        "postgres",
        "Gi5B28i.Ob{5ZxIwjzLaY\$"
      )
    }
    return cnnTFS4
  }


  private fun createCnnTFS3(): Connection {
    if (!::cnnTFS3.isInitialized) {
      Class.forName("org.postgresql.Driver")
      cnnTFS3 = DriverManager.getConnection(
        "jdbc:postgresql://34.197.142.48/tfs3",
        "postgres",
        "Gi5B28i.Ob{5ZxIwjzLaY\$"
      )
    }
    return cnnTFS3
  }
}