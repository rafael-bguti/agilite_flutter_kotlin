package info.agilite.boot.mail

import com.google.common.cache.CacheBuilder
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

object DynamicMailSenderProducer {
  private val cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(30)).build<Long, JavaMailSender>()

  fun getMailSender(config: SmtpConfig): JavaMailSender {
    return cache.get(config.empresaId) {
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