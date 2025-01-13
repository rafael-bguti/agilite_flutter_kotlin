package info.agilite.boot.templates

import org.springframework.stereotype.Service
import java.io.StringWriter

@Service
class TemplateService(
    private val freemarker: AgiliteTemplateFreeMarker
) {
  fun processTemplate(
    templateName: String,
    templateContent: String,
    dados: Map<String, Any?>
  ): String {

    val template =  freemarker.buildTemplate(templateName, templateContent)
    StringWriter().use { writer ->
      template.process(dados, writer)
      return writer.toString()
    }
  }
}