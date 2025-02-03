package gerador.cobranca.cob_antigos

import gerador.cobranca.cob_antigos.CobAntigoRepository.cnn
import java.math.BigDecimal
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CobAntigoRepository {
  fun buscarCobrancasAntigas(cnpj: String): MutableList<BigDecimal> {
    val cnn = conectarCobrancaAntigo()
    val mes6Atraz = LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val sql = " SELECT SUM(cob.valor_cobrado) as valor " +
              " FROM Cobranca cob " +
              " INNER JOIN cliente cli on cli.id = cob.cliente " +
              " WHERE COALESCE(cli.ni_cobranca, cli.ni) = '$cnpj' " +
              " AND cob.dt_vcto >= '${mes6Atraz}'" +
              " GROUP BY cli.ni_cobranca, cob.dt_vcto" +
              " ORDER BY cob.dt_vcto DESC"

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

    val url = "jdbc:postgresql://34.197.142.48/cobranca"
    val username = "postgres"
    val password = "Gi5B28i.Ob{5ZxIwjzLaY\$"

    cnn = DriverManager.getConnection(url, username, password)
    return cnn!!
  }
}
