package info.agilite.boot.crud

import info.agilite.core.extensions.localCapitalize
import info.agilite.core.extensions.localDecapitalize
import info.agilite.core.extensions.substr
import org.springframework.context.ApplicationContext

object CrudServiceResolver {
  fun createService(taskName: String, appContext: ApplicationContext): CrudService {
    val clazz = extractCrudServiceClassName(taskName)

    val result = appContext.getBean(clazz.simpleName.localDecapitalize())
    if (result !is CrudService) throw RuntimeException("Class ${clazz.name} must implement CrudService")

    return result
  }

  fun extractCrudServiceClassName(taskName: String): Class<out Any> {
    val moduleName = taskName.substr(0, 3).lowercase()
    val clazz =
      try {
        val className = taskName.substringAfter(".")

        val simpleClassName = "${className.localCapitalize()}CrudService"
        Class.forName("info.agilite.${moduleName.lowercase()}.tasks.$simpleClassName")
      } catch (e: ClassNotFoundException) {
        DefaultSduiCrudService::class.java
      }
    return clazz
  }
}