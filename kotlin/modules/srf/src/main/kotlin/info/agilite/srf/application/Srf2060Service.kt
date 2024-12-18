package info.agilite.srf.application

import info.agilite.boot.mail.Mail
import info.agilite.boot.mail.MailSenderService
import info.agilite.boot.security.UserContext
import info.agilite.boot.templates.TemplateService
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.orExc
import info.agilite.shared.entities.cas.Cas65
import info.agilite.shared.integrators.Scf02PdfGenerator
import info.agilite.srf.adapter.infra.Srf01Repository
import info.agilite.srf.adapter.infra.Srf2060Repository
import info.agilite.srf.domain.Srf2060Mail
import org.springframework.stereotype.Service

@Service
class Srf2060Service(
  val repository: Srf2060Repository,
  val srf01repo: Srf01Repository,
  val mailSender: MailSenderService,
  val templateService: TemplateService,
  val scf02PdfGenerator: Scf02PdfGenerator,
) {

  fun enviarEmailDocumentos(srf2060s: List<Srf2060Mail>) {
    val validationMessage = srf2060s.map { it.validarPraEnviarEmail() }.filterNotNull().joinToString("\n")
    if (validationMessage.isNotBlank()) throw ValidationException(validationMessage)

    val smptConfig = repository.findById(Cas65::class, UserContext.safeUser.empId)!!.toMailConfig()

    val srf01ids = srf2060s.map { it.srf01id }.distinct()
    val listaDeSrf2060Doc = repository.findDocsToSendMail(srf01ids)

    for (docToSend in listaDeSrf2060Doc) {
      val html = templateService.processTemplate(
        docToSend.cgs15.cgs15nome,
        docToSend.cgs15.cgs15template,
        mapOf(
          "srf01" to docToSend.srf01,
          "nfse" to "LINKNFSE",//TODO
        )
      )

      val mail = Mail(
        subject = docToSend.cgs15.cgs15titulo.orExc("Titulo do email não informado no CGS15"),
        html = html,
        to = "rafaelbatz@gmail.com", //srf2060mail.cgs80email!!,
        fromName = docToSend.cgs15.cgs15fromName,
        replyTo = docToSend.cgs15.cgs15replayTo,
        replyToName = docToSend.cgs15.cgs15replayToName
      )

      mailSender.sendHtml(smptConfig, mail)
      //TODO - Update na Srf01 email enviado

    }
  }
}