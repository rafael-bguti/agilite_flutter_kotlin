package gerador.cobranca.tfs4

import java.sql.Connection
import java.sql.DriverManager

class TfSam3Repository {
    fun localizarContratosTFSam3(cnpjLicencas: List<String>): Set<String> {
        val cnpjFormatados = cnpjLicencas.map { "'$it'" }.joinToString(",")
        val cnpjsEncontrados = mutableSetOf<String>()

        val sql = """
            SELECT DISTINCT COALESCE(cli.ni_cobranca, cli.ni) as cnpj  
            FROM cliente as cli
            INNER JOIN cobranca as cob ON cli.id = cob.cliente
            WHERE COALESCE(cli.ni_cobranca, cli.ni) IN ($cnpjFormatados)
            AND sistema = 1800
        """.trimIndent()

        conectarNoBancoDaCobranca().use { cnn ->
            cnn.prepareStatement(sql).use {
                val rs = it.executeQuery()
                while(rs.next()){
                    cnpjsEncontrados.add(rs.getString("cnpj"))
                }
            }
        }

        return cnpjsEncontrados
    }

    private fun conectarNoBancoDaCobranca(): Connection {
        val url = "jdbc:postgresql://34.197.142.48/cobranca"
        val username =  "postgres"
        val password = "Gi5B28i.Ob{5ZxIwjzLaY\$"

        return DriverManager.getConnection(url, username, password)
    }
}