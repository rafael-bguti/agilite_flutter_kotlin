package info.agilite.server_core.observability

import com.google.common.base.Throwables
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


interface ObserverService {
  fun programmerAttention(message: String, exception: Throwable? = null)
}

/**
 * TODO - Esse é um observador simples que apenas loga os dados. Precisa implementar um observador com Prometheus, NewRelic, CloudWatch ou qq outro player do mercado.
 */
@Service
class ObserverServiceSimpleAdapter : ObserverService {
  private val logger = LoggerFactory.getLogger(ObserverServiceSimpleAdapter::class.java)
  override fun programmerAttention(message: String, exception: Throwable? ) {
    logger.warn("Programmer Attention: $message")
    if(exception != null){
      logger.error("Programmer Attention Exception", exception)
    }
    logger.warn(Throwables.getStackTraceAsString(exception ?: Throwable()))
  }
}