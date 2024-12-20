package gerador.cobranca.tfs4

data class ContratoMultiNFe (
    val cnpjCobranca: String,
    val nfe: SitemaContratoMultiNFe?,
    val cte: SitemaContratoMultiNFe?,
)

data class SitemaContratoMultiNFe(
    val mensalidade: Double,
    val qtdDocsInclusoNaMensalidade: Int,
    val valorPorDocAdicional: Double,
)