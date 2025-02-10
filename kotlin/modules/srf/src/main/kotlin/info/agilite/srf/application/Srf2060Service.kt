package info.agilite.srf.application

import info.agilite.boot.mail.Mail
import info.agilite.boot.mail.MailAttachment
import info.agilite.boot.mail.MailSenderService
import info.agilite.boot.mail.SmtpConfig
import info.agilite.boot.security.UserContext
import info.agilite.boot.sse.SseEmitter
import info.agilite.boot.templates.TemplateService
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.format
import info.agilite.core.extensions.localCapitalize
import info.agilite.core.extensions.orExc
import info.agilite.shared.entities.cas.Cas65
import info.agilite.shared.integrators.Scf02GeradorPDFToEmail
import info.agilite.srf.domain.Srf2060Doc
import info.agilite.srf.domain.Srf2060Mail
import info.agilite.srf.infra.Srf2060Repository
import org.springframework.stereotype.Service

@Service
class Srf2060Service(
  val repository: Srf2060Repository,
  val mailSender: MailSenderService,
  val templateService: TemplateService,
  val scf02PdfGenerator: Scf02GeradorPDFToEmail,
  val sse: SseEmitter,
) {

  fun enviarEmailDocumentos(srf2060s: List<Srf2060Mail>) {
    val validationMessage = srf2060s.map { it.validarPraEnviarEmail() }.filterNotNull().joinToString("\n")
    if (validationMessage.isNotBlank()) throw ValidationException(validationMessage)

    val smptConfig = repository.findById(Cas65::class, UserContext.safeUser.empId)!!.toMailConfig()

    val srf01ids = srf2060s.map { it.srf01id }.distinct()
    val listaDeSrf2060Doc = repository.findDocsToSendMail(srf01ids)

    for (docToSend in listaDeSrf2060Doc) {
      sse.sendMessage("Enviando email para ${docToSend.cgs80.cgs80email}")
      doSendMail(docToSend, smptConfig)
    }
  }

  private fun doSendMail(docToSend: Srf2060Doc, smptConfig: SmtpConfig) {
    val html = criarCorpoDoEmail(docToSend)

    val attachments = criarAnexos(docToSend)

    val mail = Mail(
      subject = docToSend.cgs15.cgs15titulo.orExc("Titulo do email não informado no CGS15"),
      html = html,
      to = docToSend.cgs80.cgs80email!!,
      fromName = docToSend.cgs15.cgs15fromName,
      replyTo = docToSend.cgs15.cgs15replayTo,
      replyToName = docToSend.cgs15.cgs15replayToName,
      attachments = attachments,
      //bcc = "rafael@multitecsistemas.com.br"
    )
    mailSender.sendHtml(smptConfig, mail)
    repository.updateEmailSent(docToSend.srf01)
  }

  private fun criarAnexos(docToSend: Srf2060Doc): MutableList<MailAttachment> {
    val attachments = mutableListOf<MailAttachment>()
    val scf02ids = docToSend.scf02s.map { it.scf02id }
    val anexos = scf02PdfGenerator.gerarAnexoCobranca(scf02ids) // TODO passar o SCF02 aqui dentro pois está faznedo select dentro do gerarAnexoCobranca
    anexos?.values?.forEach { anexo ->
        attachments.add(MailAttachment(anexo.nome, anexo.content, anexo.contentType))
    }
    return attachments
  }

  private fun criarCorpoDoEmail(docToSend: Srf2060Doc): String {
    val mesReferencia = docToSend.srf01.srf01dtEmiss.plusMonths(-1).format("MMMM 'de' yyyy").localCapitalize()

    val html = templateService.processTemplate(
      docToSend.cgs15.cgs15nome,
      docToSend.cgs15.cgs15template,
      mapOf(
        "mesReferencia" to mesReferencia,
        "srf01" to docToSend.srf01,
        "srf011s" to docToSend.srf01.srf011s,
        "gdf10" to docToSend.gdf10,
        "scf02s" to docToSend.scf02s,
      )
    )

    return html
  }
}