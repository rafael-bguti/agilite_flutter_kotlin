package info.agilite.boot.mail

import info.agilite.core.extensions.splitToList
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailSenderService {
  private val log = LoggerFactory.getLogger(MailSenderService::class.java)

  fun sendHtml(
    config: SmtpConfig,
    mail: Mail,
  ) {
    val mailSender = DynamicMailSenderProducer.getMailSender(config)
    val mimeMessage: MimeMessage = mailSender.createMimeMessage()
    val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

    mail.to.splitToList().forEach { helper.addTo(it) }
    mail.cc?.let { ccs -> ccs.splitToList().forEach { helper.addCc(it) }}
    mail.bcc?.let { bccs -> bccs.splitToList().forEach { helper.addBcc(it) }}


    if(mail.replyTo != null) {
      helper.setReplyTo(mail.replyTo, mail.replyToName ?: mail.fromName ?: mail.replyTo)
    }else{
      config.replyTo?.let { helper.setReplyTo(it) }
    }

    if(mail.from != null || mail.fromName != null) {
      helper.setFrom(mail.from ?: config.from ?: config.user, mail.fromName ?: mail.from ?: config.user)
    } else {
      helper.setFrom(config.from ?: config.user, config.fromName ?: config.from ?: config.user)
    }

    helper.setSubject(mail.subject)
    helper.setText(mail.html, true)

    mail.attachments.forEach { attachment ->
      helper.addAttachment(attachment.name, { attachment.content.inputStream() }, attachment.contentType)
    }

    mailSender.send(mimeMessage)
    if(log.isDebugEnabled)log.debug("Email enviado para ${mail.to} - ${mail.subject}")
  }
}

data class Mail (
  val subject: String,
  val html: String,

  val to: String,
  val cc: String? = null,
  val bcc: String? = null,

  val from : String? = null,
  val fromName: String? = null,

  val replyTo: String? = null,
  val replyToName: String? = null,

  val attachments: List<MailAttachment> = emptyList()
)

data class MailAttachment(
  val name: String,
  val content: ByteArray,
  val contentType: String
)