package info.agilite.shared.xml

class SchemeValidationException(
    val mensagemResumida: String?,
    val mensagemCompleta: String,
): RuntimeException("Schema do XML inválido"){
    override val message: String
        get() = "Schema do XML inválido - $mensagemResumida \nMaiores detalhes: $mensagemCompleta"
}
