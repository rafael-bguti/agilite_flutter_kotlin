package info.agilite.boot.orm.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbTable(
  val name: String,
  val schema: String = "",
)
