package gerador.cobranca

import java.math.BigDecimal
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CobrancaRepository {
  fun buscarCobrancas(cnpj: String): MutableList<BigDecimal> {
    val cnn = conectarCobrancaAntigo()
    val mes6Atraz = LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val sql = " SELECT SUM(scf02valor) as valor " +
              " FROM agilite.Scf02 " +
              " INNER JOIN agilite.Cgs80 ON scf02entidade = cgs80id " +
              " WHERE cgs80ni = '$cnpj' " +
              " AND scf02dtVenc >= '${mes6Atraz}'" +
              " GROUP BY cgs80ni, scf02dtVenc" +
              " ORDER BY scf02dtVenc DESC"

    return cnn.prepareStatement(sql).use {
      val rs = it.executeQuery()
      val result = mutableListOf<BigDecimal>()
      while (rs.next()) {
        result.add(rs.getBigDecimal("valor"))
      }
      return result
    }
  }

  var cnn: Connection? = null
  private fun conectarCobrancaAntigo(): Connection {
    if(cnn != null) {
      return cnn!!
    }

    val url = "jdbc:postgresql://localhost/agilite_erp"
    val username = "postgres"
    val password = "Agilite1409!"

    cnn = DriverManager.getConnection(url, username, password)
    return cnn!!
  }
}
