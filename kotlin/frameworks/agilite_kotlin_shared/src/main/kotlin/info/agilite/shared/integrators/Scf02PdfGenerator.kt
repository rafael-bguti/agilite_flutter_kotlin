package info.agilite.shared.integrators

interface Scf02PdfGenerator {
  fun generatePdf(scf02id: Long): ByteArray
}