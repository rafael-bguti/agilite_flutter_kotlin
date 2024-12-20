package gerador.cobranca.tfs4

import gerador.cobranca.mesAnoReferencia
import info.agilite.core.extensions.toLowerCase
import info.agilite.core.model.LowerCaseMap
import java.sql.Connection
import java.sql.DriverManager
import java.time.format.DateTimeFormatter

class TFS4Repository {
  fun localizarConsumos(): List<ConsumoTFS4Revenda> {
    val consumos = findConsumos()

    val revendas = mutableMapOf<String, ConsumoTFS4Revenda>()
    for (consumo in consumos) {
      val revendaCnpj = consumo["revendacnpj"] as String
      val revendaRs = consumo["revendars"] as String
      if (!revendas.containsKey(revendaCnpj)) {
        revendas[revendaCnpj] = ConsumoTFS4Revenda(revendaCnpj, revendaRs, mutableListOf())
      }
    }

    val consumoPorLicenca = consumos.groupBy { it["licencacnpj"] as String }
    consumoPorLicenca.forEach { licencaEntry ->
      val consumosPorEmpresa = licencaEntry.value.map { consumo ->
        Consumo(
          consumo["empresacnpj"] as String,
          consumo["empresana"] as String,
          sistemaDFe = SistemaDFe.fromValue(consumo.getInteger("sistemadfe")!!),
          qtd = consumo.getInteger("consumo")!!,
          possuiArmazenamentoAnterior = consumo.getLong("maxid") != null
        )
      }.toMutableList()

      val consumosPorLicenca = ConsumoTFS4Licenca(
        licencaEntry.value[0]["licencanum"] as String,
        licencaEntry.value[0]["licencacnpj"] as String,
        licencaEntry.value[0]["licencars"] as String,
        consumosPorEmpresa,
      )
      revendas[licencaEntry.value[0]["revendacnpj"] as String]!!.licencas.add(consumosPorLicenca)
    }

    return revendas.values.toList()
  }

  private fun findConsumos(): List<LowerCaseMap> {
    val iniMesAnoFormatado = mesAnoReferencia.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val fimMesAnoFormatado = mesAnoReferencia.withDayOfMonth(mesAnoReferencia.lengthOfMonth()).format(
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    val sql = """
      WITH consumos AS(
        select count(*) as consumo, sistema_dfe, empresa_id
        from documentos d 
        WHERE dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado'
        group by sistema_dfe, empresa_id
      ), maxids as (
        select MAX(d.dcm_id) as maxid, sistema_dfe, empresa_id
        from documentos d 
        group by sistema_dfe, empresa_id
      )
      select m.maxid as maxid, coalesce(consumo, 0) as consumo, m.sistema_dfe as sistemadfe, con.licenca_num as licencanum, 
      con.licenca_cnpj as licencacnpj, con.licenca_rs as licencars, emp.cnpj as empresacnpj, emp.rs as empresana,
      rev.rs as revendars, rev.cnpj as revendacnpj
      from maxids as m
      inner join empresas as emp on emp.emp_id = m.empresa_id
      inner join contratos as con on emp.contrato_id = con.cnt_id
      inner join revendas as rev ON rev.rvn_id = con.revenda_id
      left join consumos as c on c.empresa_id = m.empresa_id and c.sistema_dfe = m.sistema_dfe
      order by cnt_id, emp_id
        """.trimIndent()

    val result: MutableList<Map<String, Any?>> = mutableListOf()
    conectarTFS4Db().prepareStatement(sql).use {
      val rs = it.executeQuery()
      val metadata = rs.metaData
      while (rs.next()) {
        val row = mutableMapOf<String, Any?>()
        for (i in 1..metadata.columnCount) {
          row.put(metadata.getColumnName(i), rs.getObject(i))
        }
        result.add(row)
      }
    }
    return result.map { it.toLowerCase() }
  }

  private fun conectarTFS4Db(): Connection {
    val url = "jdbc:postgresql://34.198.165.190/tfs4"
    val username = "postgres"
    val password = "Gi5B28i.Ob{5ZxIwjzLaY\$"

    return DriverManager.getConnection(url, username, password)
  }
}