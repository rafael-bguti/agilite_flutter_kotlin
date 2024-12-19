package info.agilite.boot.templates

import freemarker.cache.StringTemplateLoader
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import info.agilite.boot.security.UserContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class AgiliteTemplateFreeMarker() {
  private val stringTemplateLoader: StringTemplateLoader = StringTemplateLoader()
  private val configuration = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31)

  init {
    configuration.templateLoader = stringTemplateLoader
    configuration.locale = Locale.of("pt", "BR")
    configuration.defaultEncoding = "UTF-8"
    configuration.namingConvention = freemarker.template.Configuration.CAMEL_CASE_NAMING_CONVENTION
    configuration.templateExceptionHandler = TemplateExceptionHandler.IGNORE_HANDLER
  }


  fun buildTemplate(templateName: String, templateContent: String): Template {
    val schema = UserContext?.user?.tenantId ?: "root"
    val finalName = "$schema-$templateName"
    stringTemplateLoader.putTemplate(finalName, templateContent) // TODO voltar o ultimo parametro para 0L

    return configuration.getTemplate(finalName)
  }
}