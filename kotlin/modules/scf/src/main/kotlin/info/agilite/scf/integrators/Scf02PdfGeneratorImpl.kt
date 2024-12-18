package info.agilite.scf.integrators

import info.agilite.shared.integrators.Scf02PdfGenerator
import org.springframework.stereotype.Service

@Service
class Scf02PdfGeneratorImpl : Scf02PdfGenerator {
  override fun generatePdf(scf02id: Long): ByteArray {
    return byteArrayOf(0)
  }
}