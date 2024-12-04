package info.agilite.boot


import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

lateinit var applicationContext: ApplicationContext

@Component
class ApplicationContextProvider : ApplicationContextAware {
  override fun setApplicationContext(ctx: ApplicationContext) {
    applicationContext = ctx
  }
}