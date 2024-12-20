package gerador.cobranca.tfs4

data class ContratosMultinfe (
    val cnpjCobranca: String,
    val nfe: ContratoMultinfe?,
    val cte: ContratoMultinfe?,
)

data class ContratoMultinfe(
    val mensalidade: Double,
    val qtdDocsInclusoNaMensalidade: Int,
    val valorPorDocAdicional: Double,
)