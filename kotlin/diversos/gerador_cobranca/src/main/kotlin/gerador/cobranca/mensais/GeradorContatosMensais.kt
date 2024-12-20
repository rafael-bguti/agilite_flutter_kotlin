package gerador.cobranca.mensais

import gerador.cobranca.*
import gerador.cobranca.model.Faturamento
import info.agilite.integradores.dtos.Cobranca
import java.math.BigDecimal

class GeradorContatosMensais {
  fun gerarCobrancasMensais(): List<Faturamento>{
    return listOf(
      gerarCobrancaNuvemProcomp(),
      gerarCobrancaDPil(),
      gerarCobrancaMultiNFeMultitec()
    )
  }

  private fun gerarCobrancaNuvemProcomp(): Faturamento{
    return Faturamento(
      Utils.buildCobranca(
        NATUREZA_NUVEM, "51920700000135", CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção Nuvem Procomp", BigDecimal(500)
      )
    )
  }

  private fun gerarCobrancaDPil(): Faturamento{
    return Faturamento(Utils.buildCobranca(
      NATUREZA_DPIL, "10703519000190", CODIGO_ITEM_DPIL, "Manutenção plataforma DPil", BigDecimal(2366)
    ))
  }

  private fun gerarCobrancaMultiNFeMultitec(): Faturamento{
    return Faturamento(Utils.buildCobranca(
      NATUREZA_DPIL, "67919092000189", CODIGO_ITEM_MULTINFE, "Multinfe", BigDecimal(1155)
    ))
  }
}