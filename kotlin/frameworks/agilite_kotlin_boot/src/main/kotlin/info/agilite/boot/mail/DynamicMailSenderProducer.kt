package info.agilite.boot.mail

import com.google.common.cache.CacheBuilder
import info.agilite.boot.orm.cache.DefaultEntityCache
import info.agilite.boot.security.UserContext
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.time.Duration
import java.util.*

object DynamicMailSenderProducer {
  private val cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(30)).build<String, JavaMailSender>()

  init{
    DefaultEntityCache.addInvalidateListener { it ->
      if(it.entity.javaClass.simpleName.equals("Cas65", true)){
        cache.invalidate(computeKey(it.entity.id!!))
      }
    }
  }

  fun getMailSender(config: SmtpConfig): JavaMailSender {
    return cache.get(computeKey( config.empresaId)) {
      criarJavaMailSender(config)
    }
  }

  private fun criarJavaMailSender(config: SmtpConfig): JavaMailSender {
    val sender = JavaMailSenderImpl()
    sender.host = config.host
    sender.port = config.port
    sender.username = config.user
    sender.password = config.pass
    sender.javaMailProperties = Properties().apply {
      putAll(
        mapOf(
          "mail.smtp.auth" to config.auth,
          "mail.smtp.starttls.enable" to config.tls
        )
      )
    }
    return sender
  }

  private fun computeKey(id: Long): String {
    return UserContext.safeTentantId + id
  }
}

data class SmtpConfig(
  val empresaId: Long,
  val host: String,
  val port: Int,
  val user: String,
  val from: String? = null,
  val fromName: String? = null,
  val replyTo: String? = null,
  val pass: String,


  val tls: Boolean = true,
  val auth: Boolean = true
)