package gerador.cobranca.tfs4

enum class SistemaDFe(val value: Int) {
    //A T E N Ç Â O: NÃO ALTERAR O NOME DOS ITENS, POIS ELES SÃO USADOS PARA DEFINIR O DIRETORIO NO ARMAZENAMENTO DOS DOCUMENTOS NO S3
    NFe(10),
    MDFe(20),
    CTe(30),
    ESocial(40),
    Reinf(50);

    companion object {
        fun fromValue(value: Int): SistemaDFe {
            for (b in values()) {
                if (b.value == value) {
                    return b
                }
            }
            throw RuntimeException("SistemaDFe não encontrado para o valor: $value")
        }
    }
}