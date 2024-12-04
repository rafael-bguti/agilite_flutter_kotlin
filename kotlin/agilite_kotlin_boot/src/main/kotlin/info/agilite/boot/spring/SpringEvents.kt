package info.agilite.boot.spring

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


@Component
class SpringEvents {
  private val LOGGER: Logger = LoggerFactory.getLogger("SpringEvents.class")

  @EventListener
  fun listApplicationURIS(event: ContextRefreshedEvent) {
    if(!LOGGER.isDebugEnabled) return

    val applicationContext: ApplicationContext = event.applicationContext
    val requestMappingHandlerMapping: RequestMappingHandlerMapping = applicationContext
      .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
    val map = requestMappingHandlerMapping.handlerMethods

    LOGGER.debug("List of URIs:")
    map.forEach { (key: RequestMappingInfo?, value: HandlerMethod?) ->
      LOGGER.debug(
        "{} {}",
        key,
        value
      )
    }
  }
}