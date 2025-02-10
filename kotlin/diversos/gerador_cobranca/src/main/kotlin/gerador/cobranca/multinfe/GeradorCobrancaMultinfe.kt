package gerador.cobranca.multinfe


import com.fasterxml.jackson.core.type.TypeReference
import gerador.cobranca.*
import gerador.cobranca.daos.DAO
import info.agilite.core.extensions.format
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.bancos.dto.Cobranca
import java.io.File
import java.nio.file.Files

class GeradorCobrancaMultinfe {
  fun gerar(){
    println("Exporte o arquivo de cobrança do MultiNFe e salve em C:/lixo/cobrancas/MultiNFe-cobranca.json. Feito? (s/n)")
    val resposta = readlnOrNull()

    if (!resposta.equals("s", true)) {
      throw RuntimeException("Então exporte o arquivo de cobrança do MultiNFe e salve em C:/lixo/cobrancas/MultiNFe-cobranca.json e rode o processo novamente.")
    }

    val mnfeCobrancas = JsonUtils.fromJson(Files.readString(File("C:/lixo/cobrancas/MultiNFe-cobranca.json").toPath()), object : TypeReference<List<MNFeCobranca>>(){})
    val cobrancas = mnfeCobrancas.map { it.toCobranca() }
    cobrancasGeradas.addAll(cobrancas)

    println("Validando multinfe sem pagar...")
    validarClientesSemPagar(cobrancas)
  }

  fun validarClientesSemPagar(cobrancas: List<Cobranca>) {
    val firstDay = mesAnoReferencia.withDayOfMonth(1)
    val lastDay = mesAnoReferencia.withDayOfMonth(mesAnoReferencia.lengthOfMonth())

    val rs = DAO.queryMultiNFe(
      """
        select count(*) as qtd, aa10.aa10cnpj cnpj, aa10.aa10rs rs, aa10.aa10cnpjcob cnpjcob, aa10cob.aa10cnpj cont_cnpj, aa10cob.aa10cnpjcob cont_cnpjcob, aa10cob.aa10rs cont_rs
        from ab01
        inner join aa10 aa10 on aa10id = ab01empresa
        left join aa10 aa10cob on aa10.aa10empContrato = aa10cob.aa10id
        where ab01dtentrada between '${firstDay.format()}' and '${lastDay.format()}'
        group by aa10.aa10cnpj, aa10.aa10rs, aa10.aa10cnpjcob, aa10cob.aa10cnpj, aa10cob.aa10cnpjcob, aa10cob.aa10rs 
      """
    )

    val documentos = rs.map { DocumentosEmitidosDTO(it, SistemaConsumo.MULTINFE) }
    documentos.forEach {
      val cnpjCob = "${it.cnpj}||${it.cnpjCob}||${it.contratoCnpj}||${it.contratoCnpjCob}"
      if(!cobrancas.any { cob -> cnpjCob.contains(cob.cliente.cnpj) }){
        documentosNaoPagos.add(it)
      }
    }
  }
}