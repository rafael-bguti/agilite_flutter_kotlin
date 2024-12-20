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
      val revendaRs = consumo["revenda"] as String
      if (!revendas.containsKey(revendaCnpj)) {
        revendas[revendaCnpj] = ConsumoTFS4Revenda(revendaCnpj, revendaRs, mutableListOf())
      }
    }

    val consumoPorLicenca = consumos.groupBy { it["licencacnpj"] as String }
    consumoPorLicenca.forEach { licencaEntry ->
      val consumosPorEmpresa = licencaEntry.value.map { consumo ->
        ConsumoTFS4Empresa(
          consumo["empresacnpj"] as String,
          consumo["empresana"] as String,
          (consumo["consumonfe"] as Number).toInt(),
          (consumo["consumomdfe"] as Number).toInt(),
          (consumo["consumocte"] as Number).toInt(),
          (consumo["consumoreinf"] as Number).toInt(),
          (consumo["consumoesocial"] as Number).toInt(),
          consumo["lastnfeid"] != null,
          consumo["lastmdfeid"] != null,
          consumo["lastcteid"] != null,
          consumo["lastesocialid"] != null,
          consumo["lastreinfid"] != null,
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
        WITH consumosNFe AS (
        	SELECT count(*) as consumoNFe, empresa_id FROM documentos WHERE sistema_dfe = 10 AND dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado' group by empresa_id
        ), consumosMDFe AS (
        	SELECT count(*) as consumoMDFe, empresa_id FROM documentos WHERE sistema_dfe = 20 AND dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado' group by empresa_id
        ), consumosCTe AS (
        	SELECT count(*) as consumoCTe, empresa_id FROM documentos WHERE sistema_dfe = 30 AND dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado' group by empresa_id
        ), consumosESocial AS (
        	SELECT count(*) as consumoESocial, empresa_id FROM documentos WHERE sistema_dfe = 40 AND dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado' group by empresa_id
        ), consumosReinf AS (
        	SELECT count(*) as consumoReinf, empresa_id FROM documentos WHERE sistema_dfe = 50 AND dt_hr_armazenamento BETWEEN '$iniMesAnoFormatado' and '$fimMesAnoFormatado' group by empresa_id
        ), hasNFe AS (
            SELECT MAX(dcm_id) as lastNFeId, empresa_id FROM documentos WHERE sistema_dfe = 10 group by empresa_id
        ), hasMDFe AS (
            SELECT MAX(dcm_id) as lastMDFeId, empresa_id  FROM documentos WHERE sistema_dfe = 20 group by empresa_id
        ), hasCTe AS (
            SELECT MAX(dcm_id) as lastCTeId, empresa_id FROM documentos WHERE sistema_dfe = 30 group by empresa_id
        ), hasESocial AS (
            SELECT MAX(dcm_id) as lastESocialId, empresa_id FROM documentos WHERE sistema_dfe = 40 group by empresa_id
        ), hasReinf AS (
            SELECT MAX(dcm_id) as lastReinfId, empresa_id FROM documentos WHERE sistema_dfe = 50 group by empresa_id
        )

        select con.licenca_num as licencanum, con.licenca_cnpj as licencacnpj, con.licenca_rs as licencars,
        emp.cnpj as empresacnpj, emp.rs as empresana, 
        coalesce(consumoNFe, 0) as consumoNFe, coalesce(consumoMDFe, 0) as consumoMDFe, coalesce(consumoCTe, 0) as consumoCTe, 
        coalesce(consumoESocial, 0) as consumoESocial, coalesce(consumoReinf, 0) as consumoReinf,
        hasNFe.lastNFeId, hasMDFe.lastMDFeId, hasCTe.lastCTeId, hasESocial.lastESocialId, hasReinf.lastReinfId,
        rev.rs as revenda, rev.cnpj as revendacnpj
        from contratos as con
        inner join revendas as rev ON rev.rvn_id = con.revenda_id
        inner join empresas as emp on emp.contrato_id = con.cnt_id
        left join consumosNFe as nfe on nfe.empresa_id = emp.emp_id
        left join consumosMDFe as mdfe on mdfe.empresa_id = emp.emp_id
        left join consumosCTe as cte on cte.empresa_id = emp.emp_id
        left join consumosESocial as esocial on esocial.empresa_id = emp.emp_id
        left join consumosReinf as reinf on reinf.empresa_id = emp.emp_id
        left join hasNFe as hasnfe on hasnfe.empresa_id = emp.emp_id
        left join hasMDFe as hasmdfe on hasmdfe.empresa_id = emp.emp_id
        left join hasCTe as hascte on hascte.empresa_id = emp.emp_id
        left join hasESocial as hasesocial on hasesocial.empresa_id = emp.emp_id
        left join hasReinf as hasreinf on hasreinf.empresa_id = emp.emp_id
        where con.active = true
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