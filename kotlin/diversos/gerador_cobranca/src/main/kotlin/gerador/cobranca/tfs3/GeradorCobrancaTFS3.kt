package gerador.cobranca.tfs3

import com.fasterxml.jackson.core.type.TypeReference
import gerador.cobranca.mesAnoReferencia
import gerador.cobranca.model.Faturamento
import info.agilite.core.extensions.numbersOnly
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.dtos.Cobranca
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private const val URL_TFS3 = "http://tfs3.agilite.info/sistema/export/consumers"
private const val TFS3_KEY = "DZojWtQHPedOX9kOhThMLs3MnjkWG96KZkuwUB3kfXz3XaH4tpH6YAJCjtYqtU1I5l4Qny1ILqBiGJd1zkxNk6PKqo2a9sj5TeL"
class GeradorCobrancaTFS3 {

  fun gerar(): List<Faturamento>{
    val json = buscarJsonTFS3()
    val cobrancas = JsonUtils.fromJson(json, object : TypeReference<List<Tfs3Evento>>(){})

    trocarCnpjAlgumasCobrancas(cobrancas)
    return cobrancas.map { Faturamento(it.toCobranca(), emptyList()) } // Todo buscar os CNPJ de uso de cada cobrança
  }

  private fun trocarCnpjAlgumasCobrancas(cobrancas: List<Tfs3Evento>){
    cobrancas.forEach {
      it.cnpj = it.cnpj.numbersOnly()
      if(it.cnpj == "43460658000199"){// Tapecol
        it.cnpj = "49319528000389"
      }
      if(it.cnpj == "04860117000104"){// MSO
        it.cnpj = "61472650000124"
      }
      if(it.cnpj == "12315368000172"){// Via Mao
        it.cnpj = "12315369000172"
      }
      if(it.cnpj == "56977022000190"){// SINDICATO DOS EMPREGADOS NO COMERCIO DE LIMEIRA
        it.cnpj = "56977002000190"
      }
      if(it.cnpj == "62532452000171"){// JURP ARTIGOS
        it.cnpj = "05758809000109"
      }
      if(it.cnpj == "05908059000104"){ // Coval
        it.cnpj = "30301994000147"
      }
      if(it.cnpj == "50124395000102"){ // BAPTISTELLA
        it.cnpj = "61702569000193"
      }
    }
  }

  private fun buscarJsonTFS3(): String{
    val client: HttpClient = HttpClient.newHttpClient()

    val request = HttpRequest.newBuilder()
      .uri(URI(URL_TFS3 + "?data=" + DateTimeFormatter.ISO_DATE.format(mesAnoReferencia) + "&user_key=" + TFS3_KEY)) // Altere para o seu endpoint
      .POST(HttpRequest.BodyPublishers.noBody())
      .build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
  }
}