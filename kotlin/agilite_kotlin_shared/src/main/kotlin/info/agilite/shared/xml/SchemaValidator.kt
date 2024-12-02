package info.agilite.shared.xml

import info.agilite.shared.extensions.nullIfEmpty
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.io.StringReader
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.Validator

class SchemaValidator(
  private val schema: Schema,
) : ErrorHandler {
  private val mensagensTraduzidas: MutableList<String> = ArrayList()
  private val todasMensagens: MutableList<String> = ArrayList()

  fun validate(xml: String) {
    val validator: Validator = schema.newValidator().apply {
      errorHandler = this@SchemaValidator
    }
    validator.validate(StreamSource(StringReader(normalizeXML(xml))))

    if (todasMensagens.isNotEmpty()) {
      throw SchemeValidationException(
        mensagemResumida = mensagensTraduzidas.nullIfEmpty()?.joinToString(),
        mensagemCompleta = todasMensagens.joinToString()
      )
    }
  }

  private fun normalizeXML(_xml: String): String {
    var xml = _xml
    if (xml.startsWith("\uFEFF")) { // remove BOM
      xml = xml.substring(1)
    }
    xml = xml.replace("\r", "")
    xml = xml.replace("\n", "")
    xml = xml.replace(" standalone=\"no\"", "")

    return xml
  }

  override fun error(exception: SAXParseException) {
    if (isError(exception)) {
      traduzirMensagens(exception)
    }
  }

  override fun fatalError(exception: SAXParseException) {
    traduzirMensagens(exception)
  }

  override fun warning(exception: SAXParseException) {
    traduzirMensagens(exception)
  }

  private fun traduzirMensagens(exc: SAXParseException) {
    val mensagem = exc.message.orEmpty()
    if(mensagemPodeSerTraduzida(mensagem)) {
      val localized = exc.message.orEmpty().let { message ->
        when {
          message.contains("cvc-type.3.1.3:") -> message.replace("cvc-type.3.1.3:", "")
          message.contains("cvc-complex-type.2.4.a:") -> message.replace("cvc-complex-type.2.4.a:", "")
          message.contains("cvc-complex-type.2.4.b:") -> message.replace("cvc-complex-type.2.4.b:", "")
          else -> message
        }
      }.replace("\\{", "")
        .replace("\\}", "")
        .replace("\"", "")
        .replace("http://www.portalfiscal.inf.br/nfe:", "")
        .replace("'", "\"")
        .replace("The value", "O valor")
        .replace("The content", "O conteúdo")
        .replace("is not complete", "não está completo")
        .replace("of element", "do elemento")
        .replace("is not valid", "não é valido")
        .replace(
          "Invalid content was found starting with element",
          "Conteúdo inválido foi encontrado começando no elemento"
        )
        .replace("One of", "O(s) campo(s)")
        .replace("is expected", "era esperado antes dele")

      mensagensTraduzidas.add(localized)
    }
    todasMensagens.add("Linha: ${exc.lineNumber}, Colunas: ${exc.columnNumber} - $mensagem")
  }

  private fun mensagemPodeSerTraduzida(mensagem: String): Boolean{
    return mensagem.contains("cvc-type.3.1.3:") ||
        mensagem.contains("cvc-complex-type.2.4.a:") ||
        mensagem.contains("cvc-complex-type.2.4.b:")
  }

  private fun isError(exception: SAXParseException): Boolean {
    val message = exception.message.orEmpty()
    return !(message.startsWith("cvc-pattern-valid") ||
        message.startsWith("cvc-maxLength-valid") ||
        message.startsWith("cvc-datatype"))
  }
}