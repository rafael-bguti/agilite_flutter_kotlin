package info.agilite.server_core.orm

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
class BatchOperationProducer {
    @Bean
    @Scope("prototype")
    fun batchOperations(): BatchOperations {
        return BatchOperations()
    }
}