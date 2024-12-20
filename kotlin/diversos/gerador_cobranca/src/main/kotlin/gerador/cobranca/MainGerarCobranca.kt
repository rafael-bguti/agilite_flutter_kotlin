package gerador.cobranca

import gerador.cobranca.mensais.GeradorContatosMensais
import gerador.cobranca.model.Faturamento
import gerador.cobranca.multinfe.GeradorCobrancaMultinfe
import gerador.cobranca.tfs4.GeradorCobrancaTFS4
import gerador.cobranca.tfs3.GeradorCobrancaTFS3
import gerador.cobranca.tfs4.TFS4Repository
import java.math.BigDecimal
import java.time.LocalDate

const val CODIGO_ITEM_TFS4 = "1";
const val CODIGO_ITEM_MULTINFE = "2"
const val CODIGO_ITEM_TFS3 = "3"
const val CODIGO_ITEM_MANUTENCAO_NUVEM = "4";
const val CODIGO_ITEM_DPIL = "5";

const val NATUREZA_TFS4_REV = "Tfs4Rev";
const val NATUREZA_TFS4_BY_TFS3 = "Tfs4ByTfs3";
const val NATUREZA_MNFE = "Mnfe"
const val NATUREZA_TFS3 = "Tfs3"
const val NATUREZA_NUVEM = "ManutNuvem";
const val NATUREZA_DPIL = "Dpil";

const val NOME_FORMA_PAGAMENTO_BOLETO = "Boleto Inter"
val mesAnoReferencia = LocalDate.now().minusMonths(1)

fun main() {
  GerarCobranca().gerarCobranca()
}

val faturamentos = mutableListOf<Faturamento>()
class GerarCobranca {
  fun gerarCobranca() {


    println("Gerando cobrança MultiNFe...")
    val cobrancasMultiNFe = GeradorCobrancaMultinfe().gerar()
    faturamentos.addAll(cobrancasMultiNFe)
    println("Cobranças MultiNFe geradas: ${cobrancasMultiNFe.size}")

    println("Gerando cobrança TFS3...")
    val cobrancasTFS3 = GeradorCobrancaTFS3().gerar()
    faturamentos.addAll(cobrancasTFS3)
    println("Cobranças TFS3 geradas: ${cobrancasTFS3.size}")

    println("Gerando cobrança Mensais...")
    val cobrancasContratos = GeradorContatosMensais().gerarCobrancasMensais()
    faturamentos.addAll(cobrancasContratos)
    println("Cobranças Mensais: ${cobrancasContratos.size}")

    println("Ajustando Cobranças...")
    ajustarCobrancas()

    println("Gerando cobranças no MultiNFe a partir do SAM4...")
    val cobrancasTFS4 = gerarCobrancasDoTFS4()
    faturamentos.addAll(cobrancasTFS4)
    println("Cobranças MultiNFe by SAM4: ${cobrancasTFS4.size}")

//    ajustarVencimento();
  }

  private fun gerarCobrancasDoTFS4(): List<Faturamento>{
    val consumosTFS4PorRevenda = TFS4Repository().localizarConsumos()
    val cobrancasMultiNfeViaTFS4 = GeradorCobrancaTFS4().gerar(consumosTFS4PorRevenda);

    return cobrancasMultiNfeViaTFS4
  }


  private fun ajustarCobrancas() {
    ajustarCNPJMaliber()
    ajustarSindicatoCapivari()
    gerarNovosTitulos()
  }

  private fun ajustarCNPJMaliber(){
    faturamentos.forEach {
      if(it.cobranca.cliente.cnpj == "47938840000163"){// Tapecol
        it.cobranca.cliente.cnpj = "47938840000325"
      }
    }
  }

  private fun ajustarSindicatoCapivari(){
    //SINDICATO DOS TRABALHADORES ASSALARIADOS
    faturamentos.forEach { fat ->
      if(fat.cobranca.cliente.cnpj == "46927133000109" && fat.cobranca.natureza ==  NATUREZA_TFS3){
        fat.cobranca.itens.forEach {
          it.valor = it.valor.multiply(BigDecimal("0.9"))
        }
      }
    }
  }
  private fun gerarNovosTitulos(){

    //Silcon Materiais Elétricos e Hidraulicos
    faturamentos.add(
      Faturamento(
        Utils.buildCobranca(NATUREZA_TFS3, "43279058000129", CODIGO_ITEM_TFS3, "Serviço de suporte em informática", BigDecimal("61.6")
      )
    ))
  }
}