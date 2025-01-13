package info.agilite.shared.integrators

interface Scf02GeradorPDFToEmail {
  fun gerarAnexoCobranca(scf02ids: List<Long>): Map<Long, Scf02AnexoCobranca>?
}
class Scf02AnexoCobranca (
  val nome: String,
  val content: ByteArray,
  val contentType: String
)
