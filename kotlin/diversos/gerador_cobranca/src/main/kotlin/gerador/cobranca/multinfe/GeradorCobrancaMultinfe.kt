package gerador.cobranca.multinfe


import com.fasterxml.jackson.core.type.TypeReference
import gerador.cobranca.model.Faturamento
import info.agilite.core.json.JsonUtils
import java.io.File
import java.nio.file.Files

class GeradorCobrancaMultinfe {
  fun gerar(): List<Faturamento>{

    println("Exporte o arquivo de cobrança do MultiNFe e salve em C:/lixo/cobrancas/MultiNFe-cobranca.json. Feito? (s/n)")
    val resposta = readlnOrNull()

    if (!resposta.equals("s", true)) {
      throw RuntimeException("Então exporte o arquivo de cobrança do MultiNFe e salve em C:/lixo/cobrancas/MultiNFe-cobranca.json e rode o processo novamente.")
    }

    val cobrancas = JsonUtils.fromJson(Files.readString(File("C:/lixo/cobrancas/MultiNFe-cobranca.json").toPath()), object : TypeReference<List<MNFeCobranca>>(){})

    return cobrancas.map { Faturamento(it.toCobranca(), emptyList()) } // Todo buscar os CNPJ de uso de cada cobrança
  }
}