package info.agilite.boot.orm.annotations


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbSimpleJoin(
  /// Fornecer as propriedades que serão utilizadas para fazer o join, se quiser alterar o tipo de join, basta adicionar o tipo de join antes do nome da propriedade separado por dois pontos
  /// exemplo: "propriedade1, INNER:propriedade2, LEFT:propriedade3"
  val fieldNames: String,
)
