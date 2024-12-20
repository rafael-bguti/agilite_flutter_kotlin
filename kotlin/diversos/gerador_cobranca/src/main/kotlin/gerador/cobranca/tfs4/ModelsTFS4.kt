package gerador.cobranca.tfs4

import java.math.BigDecimal

data class ConsumoTFS4Revenda (
    val revendaCnpj: String,
    val revenda: String,
    val licencas: MutableList<ConsumoTFS4Licenca>,
)

data class ConsumoTFS4Licenca (
    val licencaNum: String,
    val licencaCNPJ: String,
    val licencaRs: String,

    val empresas: MutableList<ConsumoTFS4Empresa>,
    var cnpjCobrancaMultinfe: String? = null,
    var processado: Boolean = false,


){
    fun possuiNFeArmazenada(): Boolean {
        return empresas.any { it.possuiNFeArmazenado }
    }

    fun possuiMDFeArmazenada(): Boolean {
        return empresas.any { it.possuiMDFeArmazenado }
    }

    fun possuiCteArmazenada(): Boolean {
        return empresas.any { it.possuiCteArmazenado }
    }

    fun possuiESocialArmazenada(): Boolean {
        return empresas.any { it.possuiESocialArmazenado }
    }

    fun possuiReinfArmazenada(): Boolean {
        return empresas.any { it.possuiReinfArmazenado }
    }

    fun qtdNFe(): Int {
        return empresas.sumOf { it.qtdNfe }
    }

    fun qtdMDFe(): Int {
        return empresas.sumOf { it.qtdMDFe }
    }

    fun qtdCte(): Int {
        return empresas.sumOf { it.qtdCte }
    }

    fun qtdESocial(): Int {
        return empresas.sumOf { it.qtdESocial }
    }

    fun qtdReinf(): Int {
        return empresas.sumOf { it.qtdReinf }
    }
}

data class ConsumoTFS4Empresa (
    val empresaCNPJ: String,
    val empresaNa: String,

    val qtdNfe: Int,
    val qtdMDFe: Int,
    val qtdCte: Int,
    val qtdReinf: Int,
    val qtdESocial: Int,

    val possuiNFeArmazenado: Boolean,
    val possuiMDFeArmazenado: Boolean,
    val possuiCteArmazenado: Boolean,
    val possuiESocialArmazenado: Boolean,
    val possuiReinfArmazenado: Boolean,
){
}