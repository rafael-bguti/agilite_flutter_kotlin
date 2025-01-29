package info.agilite.boot.crud

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.jdbcDialect
import info.agilite.boot.metadata.exceptions.AutocompleteMetadataNotFoundException
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.KeyMetadataType
import info.agilite.boot.metadata.models.tasks.TaskDescr
import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQuery
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.sdui.SduiProvider
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.boot.security.UserContext
import info.agilite.core.extensions.splitToList
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.ReflectionUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

interface CrudService {
  fun findListData(taskName: String, request: CrudListRequest): CrudListResponse
//
//  fun createNewRecord(task: String): Map<String, Any?>? {
//    return null
//  }
//
  fun editRecord(taskName: String, id: Long): CrudEditResponse?
  fun update(entity: Any)
  fun insert(entity: Any)

  fun convertEntity(taskName: String, map: Map<String, *>, id: Long?): Any
//  fun delete(task: String, ids: List<Long>)
}

@Service
class DefaultSduiCrudService(
  val crudRepository: AgiliteCrudRepository
) : CrudService, SduiProvider {
  @Value("\${spring.profiles.active:default}")
  protected val activeProfile: String? = null

  //TODO adicionar um cache pra esse método, porém deixar ele desativado no ambiente de desenvolvimento
  override fun createSduiComponent(request: SduiRequest): SduiComponent {
    val taskName = request.taskName

    val entityMetadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
    val columnNames = getColumnNamesToList(taskName, entityMetadata)

    val columns = columnNames.map {
      val split = it.split(".")
      val field = defaultMetadataRepository.loadFieldMetadata(split.last())

      val fieldLength = if (field.size == 0.0 || field.foreignKeyEntity != null) 35 else field.size.toInt()
      val labelLength = field.label.length
      min(max(fieldLength, labelLength), 35)

      val width = SduiColumnWidth(SduiColumnWidthType.byCharCount, min(max(fieldLength, labelLength), 35).toDouble())
      SduiSpreadColumnComponent(
        name = it,
        label = getColumnLabel(split),
        type = field.type.frontEndType,
        options = field.options?.map { opt -> Option(opt.value, opt.label) },
        width = width,
      )
    }

    return SduiCrud(
      request.taskName,
      TaskDescr(entityMetadata.descr),
      columns,
      metadataToLoad = entityMetadata.name,
    )
  }

  override fun findListData(taskName: String, request: CrudListRequest): CrudListResponse {
    val query = createListQuery(taskName, request)
    val data  = crudRepository.findListData(query)
    return CrudListResponse(
      request.currentPage,
      request.pageSize,
      data,
    )
  }

  override fun editRecord(taskName: String, id: Long): CrudEditResponse? {
    val query = createEditQuery(taskName, id)
    val data = crudRepository.findEditData(query) ?: return null

    return CrudEditResponse(data)
  }

  override fun insert(entity: Any) {
    try {
      val tableName = EntityMappingContext.getTableAndSchema(entity!!::class.java).table
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      setDefaultValues(entityMetadata, entity)

      crudRepository.insert(entity as Any)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnSave(e)
    }
  }

  override fun update(entity: Any) {
    try {
      val tableName = EntityMappingContext.getTableAndSchema(entity!!::class.java).table
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      setDefaultValues(entityMetadata, entity)

      crudRepository.update(entity as Any)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnSave(e)
    }
  }

  override fun convertEntity(taskName: String, map: Map<String, *>, id: Long?): Any {
    val metadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
    val entityClazz = defaultMetadataRepository.loadEntityClass(metadata.name)
    val value = JsonUtils.fromMap(map, entityClazz.java)

    if(id != null){
      ReflectionUtils.setIdValue(value, id)
    }

    return value
  }

  protected fun createEditQuery(taskName: String, id: Long): DbQuery<*> {
    val entityMetadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)

    val where = WhereSimple(" ${entityMetadata.name}id = :id", mapOf("id" to id))
    val query = DbQueryBuilders.build(
      defaultMetadataRepository.loadEntityClass(entityMetadata.name),
      getColumnNamesToEdit(taskName, entityMetadata).joinToString(),
      where = where,
    )

    return query
  }

  protected fun createListQuery(taskName: String, request: CrudListRequest): DbQuery<*> {
    val entityMetadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
    val columnId = "${entityMetadata.name}id as id"

    val whereLikeSearchPair = createWhereToSearch(entityMetadata, request)
    val whereDetailedFiltersPair = createWheresToDetailedFilters(entityMetadata, request)
    val wherePadrao = getCustomWhereOnList(request, entityMetadata)
    val where = buildString {
      append(" true ")
      " AND ${AgiliteWhere.defaultWhere(entityMetadata)}"
      whereLikeSearchPair?.let { append(" AND ${it.first}") }
      whereDetailedFiltersPair?.let { append(" AND ${it.first}") }
      wherePadrao?.let { append(" AND ${it.first}") }
    }

    val filtersParams = mutableMapOf<String, Any?>()
    whereLikeSearchPair?.second?.let { filtersParams.putAll(it) }
    whereDetailedFiltersPair?.second?.let { filtersParams.putAll(it) }
    wherePadrao?.second?.let { filtersParams.putAll(it) }

    val whereSimple = WhereSimple(where, filtersParams)
    val query = DbQueryBuilders.build(
      defaultMetadataRepository.loadEntityClass(entityMetadata.name),
      getColumnNamesToList(taskName, entityMetadata).joinToString(),
      limitQuery = " LIMIT ${request.pageSize} OFFSET ${request.currentPage * request.pageSize} ",
      columnsProcessor = { "$columnId, $it" },
      where = whereSimple,
      orderBy = { getOrderByToList(entityMetadata) }
    )

    return query
  }

  protected fun getColumnNamesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
    val result = mutableListOf<String>()
    entityMetadata.fieldsToCrudList().forEach {
      if (it.foreignKeyEntity != null) {
        result.addAll(getColumNamesToJoin(taskName, it.name, it, false))
      } else {
        result.add(it.name)
      }
    }

    return result
  }

  protected fun getColumnLabel(columnNames: List<String>): String {
    return if(columnNames.size == 1){
      defaultMetadataRepository.loadFieldMetadata(columnNames[0]).label
    }else{
      val labels = mutableListOf<String>()
      columnNames.forEach {
        labels.add(defaultMetadataRepository.loadFieldMetadata(it).label)
      }
      labels.joinToString(" - ")
    }
  }

  protected fun getColumNamesToJoin(taskName: String, parentAlias: String?, parenteFkField: FieldMetadata, onEdit: Boolean): List<String> {
    val fieldsFk = if(onEdit){
      defaultMetadataRepository.loadAutocompleteFieldMetadata(parenteFkField.name, taskName).columnsToSelect.splitToList().map {
        defaultMetadataRepository.loadFieldMetadata(it)
      }
    }else{
      defaultMetadataRepository.loadEntityMetadata(parenteFkField.foreignKeyEntity!!).fieldsShowInFk()
    }
    if (fieldsFk.isEmpty()) throw RuntimeException("Não foi possível encontrar os campos para lista do cadastro da FK ${parenteFkField.foreignKeyEntity}")

    val idName = "${parenteFkField.foreignKeyEntity!!.lowercase()}id"
    val localParentAlias = parentAlias?.let { "$it." } ?: ""
    val result = mutableListOf("$localParentAlias$idName")

    fieldsFk.forEach {
      if (it.foreignKeyEntity != null) {
        result.addAll(getColumNamesToJoin(taskName,"$localParentAlias${it.name}", it, onEdit))
      } else {
        result.add("$localParentAlias${it.name}")
      }
    }
    return result
  }

  protected fun getCustomWhereOnList(request: CrudListRequest, entityMetadata: EntityMetadata): Pair<String, Map<String, Any>?>? {
    return null
  }

  private fun createWhereToSearch(entity: EntityMetadata, filters: CrudListRequest): Pair<String, Map<String, Any>?>? {
    return if (filters.search != null && !entity.fieldsSearchable().isNullOrEmpty()) {
      val columns = entity.fieldsSearchable()!!.joinToString(",") {
        jdbcDialect.coalesceString(jdbcDialect.castToString(it.name))
      }
      val where = "CONCAT(${columns}) ILIKE :search"
      Pair(where, mapOf("search" to "%${filters.search}%"))
    } else {
      null
    }
  }

  private fun createWheresToDetailedFilters(entity: EntityMetadata, filters: CrudListRequest): Pair<String, Map<String, Any?>?>? {
    return if (filters.dialogMoreFiltersValue != null) {
      val detailedFilterData = parseRequestFiltersToDetailedFilters(filters)
      val mapParams = mutableMapOf<String, Any?>()
      detailedFilterData.forEach { mapParams.putAll(it.getParameters()) }

      return Pair(
        detailedFilterData.joinToString(" AND ") { it.getWhereClause(jdbcDialect) },
        mapParams
      )
    } else {
      null
    }
  }

  private fun parseRequestFiltersToDetailedFilters(filters: CrudListRequest): List<DetailedFilterData>{
    val mapFilters = mutableMapOf<String, DetailedFilterData> ()
    filters.dialogMoreFiltersValue?.forEach { (key, value) ->
      val fieldName = key.split("_")[0]
      var localValue = value
      var operator = "ILIKE"
      if(key.contains("_")){
        operator = "BETWEEN"
      }else if(key.contains(".")){
        operator = "="
      }else{
        localValue = "%$value%"
      }

      val detailedFilterData = mapFilters[fieldName] ?: DetailedFilterData(fieldName, operator, localValue, localValue)
      if(operator == "BETWEEN"){
        if(key.contains("_ini"))
          detailedFilterData.value1 = localValue
        else if(key.contains("_fim"))
          detailedFilterData.value2 = localValue
      }

      mapFilters[fieldName] = detailedFilterData
    }

    return mapFilters.values.toList()
  }

  private fun getOrderByToList(entityMetadata: EntityMetadata): String {
    val uks = entityMetadata.findKeysByType(KeyMetadataType.uk)
    return if(uks.isNotEmpty()){
      "ORDER BY ${uks.first().fields}"
    }else{
      "ORDER BY ${entityMetadata.name}id DESC"
    }
  }

  protected fun getColumnNamesToEdit(taskName: String, entityMetadata: EntityMetadata): List<String> {
    val columns = mutableListOf<String>()
    addColumnsByTableName(taskName, entityMetadata, columns)
    return columns
  }

  private fun addColumnsByTableName(taskName: String, entityMetadata: EntityMetadata, columns: MutableList<String>, aliasPrefx: String? = null) {
    val alias = aliasPrefx?.let { "$it." } ?: ""
    entityMetadata.fields.forEach {
      if (it.foreignKeyEntity != null) {
        try{
          columns.addAll(getColumNamesToJoin(taskName,"$alias${it.name}", it, true))
        }catch (e: AutocompleteMetadataNotFoundException){
          columns.add("$alias${it.name}")
        }
      } else {
        columns.add("$alias${it.name}")
      }
    }

    entityMetadata.oneToMany.forEach { (key, value) ->
      addColumnsByTableName(taskName, defaultMetadataRepository.loadEntityMetadata(value.childJoinClass.simpleName), columns, "${alias}${key}")
    }
  }

  fun setDefaultValues(entityMetadata: EntityMetadata, entity: Any) {
    if(entityMetadata.entityHasEmpresaField()){
      ReflectionUtils.getValue<Long?>(entity as Any, entityMetadata.getEmpresaField()!!.name) ?:
        ReflectionUtils.setValue(entity as Any, entityMetadata.getEmpresaField()!!.name, UserContext.safeUser.empId)
    }
  }

  fun processConstraintViolationExceptionOnSave(rootExc: Exception): Exception {
    return rootExc


//    TODO implementar com o JDBC Spring Data
//    val constraintExc = rootExc.unwrap<DataIntegrityViolationException>() ?: return rootExc
//    try {
//      val constraintName = constraintExc.constraintName ?: throw constraintExc
//      if(constraintName.endsWith("_uk")) {
//        //TODO - Melhorar a mensagem de erro extraindo o nome das colunas (não esquecer de remover as colunas de Contrato e Empresa)
//        val msg = "Já existe um registro igual a esse. Não é possível cadastrar novamente."
//        return ValidationException(msg)
//      }
//      return rootExc
//    }catch (ignore: Exception){
//      //TODO LOGAR a exception ignore
//      return rootExc
//    }
  }

//
//  override fun delete(task: String, ids: List<Long>) {
//    try {
//      val taskName = extractTaskName(task)
//      val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata
//      crudRepository.delete(entityMetadata, ids)
//    } catch (e: Exception) {
//      throw processConstraintViolationExceptionOnDelete(e)
//    }
//  }
//
//  protected fun extractTaskName(task: String): String {
//    return task.substringAfterLast(".")
//  }
//

//

//

//

//

//
//  fun processConstraintViolationExceptionOnDelete(rootExc: Exception): Exception {
//    return rootExc
//
////    TODO implementar
////    val constraintExc = rootExc.unwrap<ConstraintViolationException>() ?: return rootExc
////
////    try {
////      val constraintName = constraintExc.constraintName
////      defaultMetadataRepository.loadEntityMetadataByFieldName(constraintName).let { entityMetadata ->
////        val msg = "Não é possível excluir o registro pois ele está em uso no 'Cadastro de ${entityMetadata.descr}'"
////        return ValidationException(msg)
////      }
////    }catch (ignore: Exception){
////      //TODO LOGAR a exception ignore
////      return rootExc
////    }
//  }
}
