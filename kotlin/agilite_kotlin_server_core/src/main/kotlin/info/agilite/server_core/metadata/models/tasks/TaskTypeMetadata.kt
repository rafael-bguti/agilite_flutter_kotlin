package info.agilite.server_core.metadata.models.tasks

enum class TaskTypeMetadata (
  val descr: String,
) {
  crud("Cadastro"),
  process("Processo"),
  report("Relatório"),
}