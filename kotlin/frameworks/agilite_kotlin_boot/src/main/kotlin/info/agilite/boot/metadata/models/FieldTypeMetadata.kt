package info.agilite.boot.metadata.models


/// Enum com os tipos de campos que podem ser criados,
/// não alterar o nome par CamelCase pois é usado no JSON, e pode ocorrer problemas de impedância com o front-end
enum class FieldTypeMetadata(
  val kotlinType: String,
  val postgreType: String,
  val frontEndType: String
) {
  id("Long", "SERIAL","int"),
  string("String", "VARCHAR","string"),
  text("String", "TEXT","string"),
  int("Int", "INTEGER","int"),
  long("Int", "LONG","int"),
  decimal("BigDecimal", "DECIMAL","double"),
  money("BigDecimal", "DECIMAL","double"),
  date("LocalDate", "DATE","date"),
  dateTime("LocalDateTime", "TIMESTAMP", "timestamp"),
  time("LocalTime", "TIME", "time"),
  boolean("Boolean", "BOOLEAN", "bool"),
  fk("Long", "BIGINT", "double"),
  json("TableMap", "jsonb", "map"),
}