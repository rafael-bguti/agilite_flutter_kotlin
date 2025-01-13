package gerador.cobranca

import com.google.common.io.Files
import gerador.cobranca.mensais.GeradorContatosMensais
import gerador.cobranca.multinfe.GeradorCobrancaMultinfe
import gerador.cobranca.tfs4.GeradorCobrancaTFS4
import gerador.cobranca.tfs3.GeradorCobrancaTFS3
import info.agilite.core.extensions.moveUntilUtilDay
import info.agilite.core.json.JsonUtils
import info.agilite.integradores.dtos.Cobranca
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate

const val CODIGO_ITEM_TFS4 = "1";
const val CODIGO_ITEM_MULTINFE = "2"
const val CODIGO_ITEM_TFS3 = "3"
const val CODIGO_ITEM_MANUTENCAO_NUVEM = "4";
const val CODIGO_ITEM_DPIL = "5";
const val CODIGO_ITEM_PROGRAMACAO = "6";
const val CODIGO_ITEM_TFS4_CLI = "7";

const val NATUREZA_TFS4_REV = "TFS4_REV";
const val NATUREZA_TFS4_CLI = "TFS4_CLI";
const val NATUREZA_MNFE = "MNFE"
const val NATUREZA_MNFE_IN_SAM4 = "MNFE_IN_SAM4"
const val NATUREZA_TFS3 = "TFS3"
const val NATUREZA_NUVEM = "MANUT_NUVEM";
const val NATUREZA_DPIL = "DPIL";
const val NATUREZA_SOFTWARE = "SOFTWARE";
const val NATUREZA_FREELA = "FREELA";

const val NOME_FORMA_PAGAMENTO_BOLETO = "Boleto Inter"
val mesAnoReferencia = LocalDate.now().minusMonths(1)

fun main() {
  GerarCobranca().gerarCobranca()
}

val cobrancasGeradas = mutableListOf<Cobranca>()
val documentosNaoPagos = mutableListOf<DocumentosEmitidosDTO>()
class GerarCobranca {
  fun gerarCobranca() {
    println("Gerando cobrança MultiNFe...")
    GeradorCobrancaMultinfe().gerar()

    println("Gerando cobrança TFS3...")
    GeradorCobrancaTFS3().gerar()

    println("Gerando cobrança Mensais...")
    GeradorContatosMensais().gerarCobrancasMensais()

    println("Ajustando Cobranças...")
    ajustarCobrancas()

    println("Gerando cobranças no MultiNFe a partir do SAM4...")
    GeradorCobrancaTFS4().gerar()

    ajustarVencimento()
    Files.write(JsonUtils.toJson(cobrancasGeradas).toByteArray(), File("c:\\lixo\\cobrancas\\cobrancas.json"))
    println("Arquivo criado em: c:\\lixo\\cobrancas\\cobrancas.json")
    println("VALOR TOTAL: ${cobrancasGeradas.sumOf { it.total() }}")

    println("\n\n")
    println("* * * * * * * * A T E N Ç Ã O * * * * * * * *")
    println("* * * * CLIENTES USANDO SEM PAGAR * * * * * *")
    println(documentosNaoPagos.joinToString("\n"))
    println("* * * * CLIENTES USANDO SEM PAGAR * * * * * *")
    println("* * * * * * * * A T E N Ç Ã O * * * * * * * *")

    println("Arquivo de cobrança gerado com sucesso! -> c:\\lixo\\cobrancas\\cobrancas.json")
  }

  private fun ajustarVencimento(){
    cobrancasGeradas.forEach { cob ->
      cob.formasPagamento.forEach {
        if(it.dataVencimento.dayOfMonth == 20){
         it.dataVencimento = it.dataVencimento.moveUntilUtilDay()
        }
      }
    }
  }

  private fun ajustarCobrancas() {
    ajustarCNPJMaliber()
    ajustarSindicatoCapivari()
    gerarNovosTitulos()
  }

  private fun ajustarCNPJMaliber(){
    //TODO Jan de 2025 não funcionou
    cobrancasGeradas.forEach {
      if(it.cliente.cnpj == "47938840000163"){// Maliber
        it.cliente.cnpj = "47938840000325"
      }
    }
  }

  private fun ajustarSindicatoCapivari(){
    //SINDICATO DOS TRABALHADORES ASSALARIADOS DE CAPIVARI
    cobrancasGeradas.forEach { cob ->
      if(cob.cliente.cnpj == "46927133000109" && cob.natureza ==  NATUREZA_TFS3){
        cob.itens.forEach {
          it.valor = it.valor.multiply(BigDecimal("0.9"))
        }
        cob.formasPagamento.forEach {
          it.valor = it.valor.multiply(BigDecimal("0.9"))
        }
      }
    }
  }
  private fun gerarNovosTitulos(){

    //Silcon Materiais Elétricos e Hidraulicos
    cobrancasGeradas.add(
      Utils.buildCobranca(NATUREZA_TFS3, "43279058000129", CODIGO_ITEM_TFS3, "Serviço de suporte em informática", BigDecimal("61.6")
    ))
  }
}