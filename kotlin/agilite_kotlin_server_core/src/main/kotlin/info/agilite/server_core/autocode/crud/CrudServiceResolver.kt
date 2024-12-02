package info.agilite.server_core.autocode.crud

import info.agilite.shared.extensions.localCapitalize
import info.agilite.shared.extensions.localDecapitalize
import info.agilite.shared.extensions.substr
import org.springframework.context.ApplicationContext

object CrudServiceResolver {
    fun createService(taskName: String, appContext: ApplicationContext): CrudService<*> {
      val moduleName = taskName.substr(0, 3).lowercase()
      val clazz =
      try {
//        val subModel = taskName.substringBefore(".")
        val className = taskName.substringAfter(".")

        val simpleClassName = "${className.localCapitalize()}CrudService"
        Class.forName("info.agilite.erp.modules.${moduleName.lowercase()}.services.crud.$simpleClassName")
      } catch (e: ClassNotFoundException) {
        DefaultCrudService::class.java
      }

      val result = appContext.getBean(clazz.simpleName.localDecapitalize())
      if(result !is CrudService<*>) throw RuntimeException("Class ${clazz.name} must implement CrudService")

      return result
    }
}