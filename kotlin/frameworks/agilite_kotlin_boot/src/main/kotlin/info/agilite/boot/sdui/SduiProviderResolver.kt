package info.agilite.boot.sdui

import info.agilite.boot.crud.CrudServiceResolver
import info.agilite.core.extensions.localDecapitalize
import org.springframework.context.ApplicationContext

object SduiProviderResolver {
  fun sduiProvider(request: SduiRequest, appContext: ApplicationContext): SduiProvider {
    var providerClassName: String? = request.providerClassName
    val clazz: Class<*> = if (providerClassName != null) {
        Class.forName(providerClassName)
      }else if(request.type == SduiRequestType.CRUD){
          extractCrudClass(request)
      }else{
        throw RuntimeException("Provider class name is not defined to request ${request.taskName}")
      }

    val result = appContext.getBean(clazz.simpleName.localDecapitalize())
    if(result !is SduiProvider) throw RuntimeException("Class ${clazz.name} must implement SduiProvider")

    return result
  }

  fun extractCrudClass(request: SduiRequest): Class<*> {
    return CrudServiceResolver.extractCrudServiceClassName(request.taskName)
  }
}