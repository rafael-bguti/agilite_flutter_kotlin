package info.agilite.server_core.orm.query

import info.agilite.shared.extensions.concatIfNotNull
import info.agilite.shared.extensions.splitToList
import info.agilite.server_core.defaultMetadataRepository
import info.agilite.server_core.metadata.exceptions.FieldMetadataNotFoundException
import info.agilite.server_core.orm.EntityMappingContext
import info.agilite.server_core.orm.WhereClause
import info.agilite.server_core.orm.jdbc.mappers.OneToMany
import info.agilite.server_core.orm.jdbc.mappers.OneToManyManager
import java.lang.reflect.Field
import kotlin.reflect.KClass

data class DbQuery<T: Any>(
  val clazz: KClass<T>,
  val columns: String? = null,
  val fetchJoins: List<DbJoin>? = emptyList<DbJoin>(),
  val simpleJoin: String? = null,
  val where: WhereClause? = null,
  val scraps: String? = null,
  val orderBy: (() -> String)? = null,
  val limitQuery: String? = null,
  val columnsProcessor: ((columns: String) -> String)? = null,
) {
  fun sql(): String {
    val columns = processColumns()
    val processedOrderBy = processOrderBy()
    return """ SELECT $columns 
      | FROM ${EntityMappingContext.getTableAndSchemaQuery(clazz.java)} ${processJoins()}
      | ${simpleJoin ?: ""}
      | ${where?.where("WHERE") ?: ""}
      | ${scraps ?: ""}
      | $processedOrderBy
      | ${limitQuery ?: ""}""".trimMargin()
  }

  internal fun oneToManyManager(): OneToManyManager? {
    if(!hasOneToMany()) return null

    val oneToManyManager = OneToManyManager(columnId)
    fetchJoins?.filter { it.processedIsOneToManyJoin }?.forEach { oneToManyManager.addOneToMany(it.buildOneToMany()) }

    return oneToManyManager
  }

  fun getParams(): Map<String, Any?> {
    return where?.params ?: emptyMap()
  }

  private fun processColumns(): String {
    val columnsResult = buildString {
      append(extractColumns(null, clazz.java, if(columns == "*") null else columns))
      fetchJoins?.forEach { join ->
        append(", ")
        append(join.processColumns(clazz.java, null))
      }
    }

    if(columnsProcessor != null) return columnsProcessor.invoke(columnsResult)
    return columnsResult
  }

  private fun processOrderBy(): String {
    if(orderBy != null) return orderBy.invoke()

    val joinsOrderBy = fetchJoins?.filter { it.processedIsOneToManyJoin }?.joinToString { "${it.processedChildTable}id" }
    return "ORDER BY $columnId${joinsOrderBy.concatIfNotNull(", ")}"
  }
  private fun processJoins(): String {
    return fetchJoins?.joinToString(" ") { it.processJoins(clazz.java, false, null) } ?: " "
  }
  private fun hasOneToMany(): Boolean {
    return fetchJoins?.any { it.processedIsOneToManyJoin } ?: false
  }

  private val columnId: String
    get() {
      return "${EntityMappingContext.getTableAndSchemaQuery(clazz.java).lowercase()}id"
    }


}

data class DbJoin(
  val parenteFkColumn: String,
  val columns: String? = null,
  val joins: List<DbJoin> = mutableListOf<DbJoin>(),
  val childClass: KClass<*>? = null,
  val joinColumn: String? = null,
  val leftJoin: Boolean? = null,
) {
  private lateinit var processedChildClass: Class<*>
  private lateinit var processedChildJoinColumn: String
  internal lateinit var processedChildTable: String
  var processedIsOneToManyJoin: Boolean = false

  internal fun processColumns(parentClass: Class<*>, parentColumnAlias: String?): String {
    val parentField = parentClass.declaredFields.find {it.name == parenteFkColumn }
    if (parentField == null) throw IllegalArgumentException("Property $parenteFkColumn not found in class ${parentClass.simpleName}")

    val joinClass = parentField.type
    val isCollection = joinClass.isAssignableFrom(Set::class.java) || parentField.type.isAssignableFrom(List::class.java)
    val parentTableName = EntityMappingContext.getTableAndSchema(parentClass).table
    val childJoinData = extractChildJoinData(parentTableName, parentField, isCollection)

    processedChildClass = childJoinData.first
    processedChildJoinColumn = childJoinData.second
    processedIsOneToManyJoin = isCollection
    processedChildTable = EntityMappingContext.getTableAndSchemaQuery(processedChildClass).lowercase()

    return buildString {
      val preColumnAlias = genPreColumnAlias(parentColumnAlias, isCollection)
      append(extractColumns(parenteFkColumn, processedChildClass, columns, preColumnAlias))
      joins?.forEach { join ->
        append(", ")
        append(join.processColumns(processedChildClass, preColumnAlias))
      }
    }
  }

  internal fun processJoins(parentClass: Class<*>, parentIsLeft: Boolean, parentAlias: String?): String{
    return buildString {
      val isLeft = if(parentIsLeft) true else isLeftJoin()

      append(if (isLeft) " LEFT JOIN " else " INNER JOIN ")
      append(processedChildTable)
      append(" AS $parenteFkColumn ON ")
      if(processedIsOneToManyJoin){
        val parentTable = EntityMappingContext.getTableAndSchemaQuery(parentClass).lowercase()
        append("$parenteFkColumn.${processedChildJoinColumn} = ")
        append(parentAlias?.let { "$it.${parentTable}id" } ?: "${parentTable}id " )
      }else{
        append("$parenteFkColumn.${processedChildTable}id =")
        append(parentAlias?.let { "$it.$processedChildJoinColumn" } ?: processedChildJoinColumn)
      }

      joins?.forEach { join ->
        append(join.processJoins(processedChildClass, isLeft, parenteFkColumn))
      }
    }
  }

  internal fun addJoin(dbJoin: DbJoin) {
    if(joins !is MutableList)throw UnsupportedOperationException("Joins is not a MutableList")
    joins.add(dbJoin)
  }

  private fun isLeftJoin(): Boolean {
    if(leftJoin != null) return leftJoin!!

    try {
      val isRequired = defaultMetadataRepository.loadFieldMetadata(parenteFkColumn).required
      return !isRequired
    }catch (e: FieldMetadataNotFoundException) {
      return true
    }
  }

  private fun extractChildJoinData(parentTableName: String, parentField: Field, isCollection: Boolean): Pair<Class<*>, String> {
    if (childClass != null) return Pair(childClass.java, joinColumn ?: parenteFkColumn)
    return if (isCollection) {
      defaultMetadataRepository.loadEntityMetadata(parentTableName).getOneToManyByName(parentField.name)?.let {
        Pair(it.childJoinClass, it.childJoinColumn)
      } ?: throw IllegalArgumentException("Property $parenteFkColumn in class ${parentTableName} is a collection and must have a @DbOneToMany annotation")
    } else {
      Pair(parentField.type, parenteFkColumn)
    }
  }

  protected open fun genPreColumnAlias(parentColumnAlias: String?, isSet: Boolean): String? {
    val suffix = if (isSet) "[]" else ""
    return parentColumnAlias?.let { "$it.$parenteFkColumn$suffix" } ?: "$parenteFkColumn$suffix"
  }

  internal fun buildOneToMany(): OneToMany {
    val oneToMany = OneToMany(parenteFkColumn, "${processedChildTable}id")
    joins?.filter { it.processedIsOneToManyJoin }?.forEach { oneToMany.addOneToMany(it.buildOneToMany()) }
    return oneToMany
  }
}

private fun extractColumns(
  tableAlias: String?,
  clazz: Class<*>,
  columns: String?,
  preColumnsAlias: String? = null,
): String {
  if (columns.isNullOrBlank()) {
    return addAliasOnColumns(tableAlias, EntityMappingContext.getColumnsQuery(clazz), preColumnsAlias)
  }else{
    if(columns.contains("*")){
      return tableAlias?.let { "$it.*" } ?: "*"
    }
    return addAliasOnColumns(tableAlias, columns.splitToList(","), preColumnsAlias)
  }

}

private fun addAliasOnColumns(tableAlias: String?, columns: List<String>, preColumnsAlias: String? = null): String {
  val prefixAs = (preColumnsAlias ?: tableAlias).let {
    if(it != null) "$it." else ""
  }
  val localTableAlias = tableAlias?.let { "$it." } ?: ""

  return columns.joinToString(",") {
    if(it.contains(".")){
      val columnName = it.substringBefore(".")
      "$localTableAlias$columnName AS \"$prefixAs$it\""
    }else{
      "$localTableAlias$it AS \"$prefixAs$it\""
    }
  }
}