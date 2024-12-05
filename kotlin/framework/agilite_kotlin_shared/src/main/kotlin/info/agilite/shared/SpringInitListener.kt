package info.agilite.shared

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.jdbcDialect
import info.agilite.boot.metadata.data.MetadataRepositoryImpl
import info.agilite.boot.orm.dialects.PostgresDialect
import info.agilite.shared.metadata.DicDados
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class SpringInitListener : ApplicationListener<ApplicationReadyEvent> {
  override fun onApplicationEvent(event: ApplicationReadyEvent) {
    defaultMetadataRepository = MetadataRepositoryImpl(DicDados())
    jdbcDialect = PostgresDialect()
  }
}
