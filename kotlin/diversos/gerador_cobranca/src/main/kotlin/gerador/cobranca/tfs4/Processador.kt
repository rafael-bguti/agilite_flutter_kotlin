package gerador.cobranca.tfs4

import transmissor.cobranca.geradores.montarHistorico
import transmissor.cobranca.models.Cobranca
import transmissor.cobranca.models.CobrancaItem
import transmissor.cobranca.models.Consumo
import transmissor.core.models.SistemaDFe

abstract class Processador {
    abstract fun processar(consumo: ConsumoTFS4Licenca)
}

class IgnorarCobranca : Processador() {
    override fun processar(consumo: ConsumoTFS4Licenca) {
        return null
    }
}

class ValorZeradoAte100DocsE1Empresa : Processador() {
    override fun processar(consumo: Consumo): Cobranca {
        val consumoTotal = consumo.consumoPorEmpresas.sumOf { it.qtdTotal() }

        val sistemas = mutableListOf<SistemaDFe>()
        if(consumo.possuiNFeArmazenada()) sistemas.add(SistemaDFe.NFe)
        if(consumo.possuiMDFeArmazenada()) sistemas.add(SistemaDFe.MDFe)
        if(consumo.possuiCteArmazenada()) sistemas.add(SistemaDFe.CTe)
        if(consumo.possuiESocialArmazenada()) sistemas.add(SistemaDFe.ESocial)
        if(consumo.possuiReinfArmazenada()) sistemas.add(SistemaDFe.Reinf)

        val item = CobrancaItem(
            false,
            montarHistorico(consumo.consumoPorEmpresas, consumoTotal, sistemas),
            consumoTotal,
            0.0
        )

        if(consumoTotal > 100){
            println("ATENÇÃO: A empresa ${consumo.licencaCNPJ}-${consumo.licencaRs} ultrapassou o limite de 100 documentos com valor ZERADO")
        }
        if(consumo.consumoPorEmpresas.size > 1){
            println("ATENÇÃO: A empresa ${consumo.licencaCNPJ}-${consumo.licencaRs} possui mais de uma empresa utilizando o sistema com valor ZERADO")
        }

        return Cobranca(consumo.licencaCNPJ, consumo.licencaRs,  consumo.revenda, listOf(item), consumo.cnpjCobrancaMultinfe)
    }
}