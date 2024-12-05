package info.agilite.boot.metadata.models.tasks

enum class TaskTypeMetadata (
  val descr: String,
) {
  crud("Cadastro"),
  process("Processo"),
  report("Relatório"),
}