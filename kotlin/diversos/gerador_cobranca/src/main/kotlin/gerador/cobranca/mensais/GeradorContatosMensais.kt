package gerador.cobranca.mensais

import gerador.cobranca.*
import info.agilite.integradores.dtos.Cobranca
import java.math.BigDecimal

class GeradorContatosMensais {
  fun gerarCobrancasMensais() {
    cobrancasGeradas.addAll(
      mutableListOf(
        gerarCobrancaDPil(),
        gerarCobrancaMultiNFeMultitec()
      ) +
        gerarCobrancaNuvem()
        +
        gerarFaturamentoPessoal()
        +
        gerarCobrancaNuvemPorFora()
    )
  }

  //TODO Multitec
  private fun gerarFaturamentoPessoal(): List<Cobranca> {
    return listOf(
//      //Eltech Dentro
//      Utils.buildCobranca(
//        NATUREZA_SOFTWARE, "52104228000125",
//        CODIGO_ITEM_PROGRAMACAO, "Programação",
//        BigDecimal(8000),
//      ),
//
//      //Multitec Dentro
//      Utils.buildCobranca(
//        NATUREZA_SOFTWARE, "67919092000189",
//        CODIGO_ITEM_PROGRAMACAO, "Programação",
//        BigDecimal(4000)
//      ),
//
//      //Multitec fora
//      Utils.buildCobranca(
//        NATUREZA_FREELA, "67919092000189",
//        CODIGO_ITEM_PROGRAMACAO, "Programação",
//        BigDecimal(3000), NOME_FORMA_PAGAMENTO_PIX
//      ),
    )
  }

  private fun gerarCobrancaNuvemPorFora(): List<Cobranca> {
    return listOf(
      (Utils.buildCobranca(
        NATUREZA_FREELA,
        "10483676000137",
        CODIGO_ITEM_MANUTENCAO_NUVEM,
        "Manutenção nuvem Potencial",
        BigDecimal(400),
        forma = NOME_FORMA_PAGAMENTO_PIX,
        obs = "Servidores Soluc e Maleo"
      )),//Marcelo nuvem Hostgator
    )
  }

  private fun gerarCobrancaNuvem(): List<Cobranca> {
    return listOf(
      //Procomp
      Utils.buildCobranca(NATUREZA_NUVEM, "51920700000135", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção Nuvem Procomp", BigDecimal(500)),
      //Eltech
      Utils.buildCobranca(NATUREZA_NUVEM, "52104228000125", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção Nuvem AWS", BigDecimal(500)),
      //Edson
      Utils.buildCobranca(NATUREZA_NUVEM, "37013890000186", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção Nuvem AWS", BigDecimal(500)),
      //Portencial
      Utils.buildCobranca(NATUREZA_NUVEM, "10483676000137", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção Nuvem AWS", BigDecimal(500)),
      //Mais verdes
      Utils.buildCobranca(NATUREZA_NUVEM, "10483676000137", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção nuvem Maisverdes", BigDecimal(817.6)),
    )
  }

  private fun gerarCobrancaDPil(): Cobranca {
    return (Utils.buildCobranca(
      NATUREZA_DPIL, "10703519000190", CODIGO_ITEM_DPIL, "Manutenção plataforma DPil", BigDecimal(2366)
    ))
  }

  private fun gerarCobrancaMultiNFeMultitec(): Cobranca {
    return (Utils.buildCobranca(
      NATUREZA_MNFE, "67919092000189", CODIGO_ITEM_MULTINFE, "Multinfe", BigDecimal(1155)
    ))
  }
}