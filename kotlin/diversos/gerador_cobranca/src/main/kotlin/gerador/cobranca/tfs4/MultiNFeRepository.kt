package gerador.cobranca.tfs4

import gerador.cobranca.mesAnoReferencia
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import kotlin.math.max


class MultiNFeRepository {
    fun localizarContratosMultiNFe(cnpjLicencas: List<String>): Map<String, ContratoMultiNFe> {
        val cnn = conectarNoBancoDoMultiNFe();
        val contratos = mutableMapOf<String, ContratoMultiNFe>()
        for(cnpj in cnpjLicencas) {
            val sql = """
                SELECT aa101.*, COALESCE(aa10cnpjcob, aa10cnpj) as cnpjCob 
                FROM AA101
                INNER JOIN Aa10 ON aa10id = aa101empresa
                WHERE aa101empresa IN (
                    SELECT COALESCE(aa10empContrato, aa10id)
                    FROM Aa10
                    WHERE aa10cnpj = '$cnpj'
                ) AND aa101servico NOT IN (3, 0) AND aa10ativa = 1
            """.trimIndent()

            val rows: MutableList<Map<String, Any?>> = mutableListOf()

            cnn.prepareStatement(sql).use {
                val rs = it.executeQuery()
                val metadata = rs.metaData
                while(rs.next()){
                    val row = mutableMapOf<String, Any?>()
                    for(i in 1..metadata.columnCount){
                        row.put(metadata.getColumnName(i), rs.getObject(i))
                    }
                    rows.add(row)
                }
            }

            if(rows.size == 0) continue

            var contratoNFe: SitemaContratoMultiNFe? = null
            var contratoCTe: SitemaContratoMultiNFe? = null

            val cnpjCob = rows[0]["cnpjcob"] as String
            for(row in rows){
                val dataInativacao = (row["aa101dtinativacao"] as Date).toLocalDate()
                if(dataInativacao.isBefore(mesAnoReferencia)){
                    continue
                }

                val contrato = SitemaContratoMultiNFe(
                    mensalidade = (row["aa101vlrmensal"] as Number).toDouble(),
                    qtdDocsInclusoNaMensalidade = (row["aa101qtdenvio"] as Number).toInt(),
                    valorPorDocAdicional = max((row["aa101adienvio"] as Number).toDouble(), (row["aa101adiarm"] as Number).toDouble()),
                )
                when(row["aa101tipo"]){
                    1 -> contratoNFe = contrato
                    2 -> contratoCTe = contrato
                }
            }

            if(contratoNFe != null || contratoCTe != null){
                contratos.put(cnpj, ContratoMultiNFe(
                    cnpjCob,
                    nfe = contratoNFe,
                    cte = contratoCTe,
                )
                )
            }
        }

        return contratos
    }

    private fun conectarNoBancoDoMultiNFe(): Connection {
        val url = "jdbc:postgresql://34.198.165.190/multinfe"
        val username =  "postgres"
        val password = "Gi5B28i.Ob{5ZxIwjzLaY\$"

        return DriverManager.getConnection(url, username, password)
    }
}