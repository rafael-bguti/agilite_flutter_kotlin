package info.agilite.boot.crud

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.jdbcDialect
import info.agilite.boot.metadata.MetadataUtils
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
import info.agilite.boot.sdui.component.*
import info.agilite.boot.security.UserContext
import info.agilite.core.extensions.nest
import info.agilite.core.extensions.splitToList
import info.agilite.core.extensions.toLowerCase
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.MapUtils
import info.agilite.core.utils.ReflectionUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface CrudService<T> {
  val createSduiFormOnEdit : Boolean get() = false

  fun findListData(taskName: String, request: CrudListRequest): CrudListResponse
  fun parseData(data: List<MutableMap<String, Any?>>): List<MutableMap<String, Any?>> { return data }

  fun createSduiEditForm(taskName: String, id: Long?): SduiComponent? {
    if (createSduiFormOnEdit) {
      return SduiSizedBox() // TODO criar o form de edição padrão
    }
    return null // TODO criar o form de edição padrão
  }

  fun createNewRecord(task: String): CrudEditResponse {
    return CrudEditResponse(buildMapOnCreateNewRecord(task), createSduiEditForm(task, null))
  }
  fun buildMapOnCreateNewRecord(task: String): Map<String, Any?> { return emptyMap() }

  fun editRecord(taskName: String, id: Long): CrudEditResponse {
    return CrudEditResponse(buildMapOnEditRecord(taskName, id), createSduiEditForm(taskName, id))
  }
  fun buildMapOnEditRecord(taskName: String, id: Long): Map<String, Any?>?

  fun validate(entity: T) {}
  fun save(entity: T)

  fun convertEntity(taskName: String, map: Map<String, *>, id: Long?): T
  fun delete(task: String, ids: List<Long>)
}

@Service
class DefaultSduiCrudService<T>(
  val crudRepository: AgiliteCrudRepository
) : CrudService<T>, SduiProvider {
  @Value("\${spring.profiles.active:default}")
  protected val activeProfile: String? = null

  //TODO adicionar um cache pra esse método, porém deixar ele desativado no ambiente de desenvolvimento
  override fun createSduiComponent(request: SduiRequest): SduiComponent {
    val taskName = request.taskName

    val entityMetadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
    val columnQuery = getColumnQueriesToList(taskName, entityMetadata)
    val columns = convertColumQueryToSduiColumn(columnQuery)

    return SduiCrud(
      request.taskName,
      TaskDescr(entityMetadata.descr),
      columns,
      metadataToLoad = entityMetadata.name,
      formBody = if(createSduiFormOnEdit || activeProfile == "dev") null else createSduiEditForm(request.taskName, null),
    )
  }

  protected fun convertColumQueryToSduiColumn(columnQuery: List<String>): MutableList<SduiColumn> {
   return SduiParser.parseQueryToColumns(columnQuery.joinToString()).toMutableList()
  }

  override fun findListData(taskName: String, request: CrudListRequest): CrudListResponse {
    val query = createListQuery(taskName, request)
    val data  = crudRepository.findListData(query)
    val parsedData = parseData (data)
    return CrudListResponse(
      request.currentPage,
      request.pageSize,
      parsedData,
    )
  }

  override fun buildMapOnEditRecord(taskName: String, id: Long): Map<String, Any?>? {
    val query = createEditQuery(taskName, id)
    return crudRepository.findEditData(query)?.nest()
  }

  override fun save(entity: T) {
    try {
      val tableName = EntityMappingContext.getTableAndSchema(entity!!::class.java).table
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      setDefaultValues(entityMetadata, entity)

      validate(entity)
      crudRepository.save(entity as Any)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnSave(e)
    }
  }

  override fun convertEntity(taskName: String, map: Map<String, *>, id: Long?): T {
    val metadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
    val entityClazz = defaultMetadataRepository.loadEntityClass(metadata.name)
    val value = JsonUtils.fromMap(map, entityClazz.java)

    if(id != null){
      ReflectionUtils.setIdValue(value, id)
    }

    return value as T
  }


  override fun delete(taskName: String, ids: List<Long>) {
    try {
      val entityMetadata = defaultMetadataRepository.loadEntityMetadataByCrudTaskName(taskName)
      crudRepository.delete(entityMetadata, ids)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnDelete(e)
    }
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

    val whereLikeSearchPair = createWhereToSearch(taskName, entityMetadata, request)
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
    val columnsQuery = getColumnQueriesToList(taskName, entityMetadata).joinToString()
    val query = DbQueryBuilders.build(
      defaultMetadataRepository.loadEntityClass(entityMetadata.name),
      columnsQuery,
      limitQuery = " LIMIT ${request.pageSize} OFFSET ${request.currentPage * request.pageSize} ",
      columnsProcessor = { "$columnId, $it" },
      where = whereSimple,
      orderBy = { getOrderByToList(entityMetadata) }
    )

    return query
  }

  protected fun getColumnQueriesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
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
    val result = if(onEdit) mutableListOf("$localParentAlias$idName") else mutableListOf()

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

  private fun createWhereToSearch(taskName: String, entity: EntityMetadata, filters: CrudListRequest): Pair<String, Map<String, Any>?>? {
    return if (filters.search != null) {
      val columns = getColumnQueriesToList(taskName, entity).joinToString(",") {
        jdbcDialect.coalesceString(jdbcDialect.castToString(it))
      }
      val where = "CONCAT(${columns}) ILIKE :search"
      Pair(where, mapOf("search" to "%${filters.search}%"))
    } else {
      null
    }
  }

  protected fun buildMoreFilterKey(fieldName: String, type: MoreFiltersType, indexOnBetween: Int? = null): String {
    if(type == MoreFiltersType.BETWEEN && indexOnBetween == null){
      throw RuntimeException("O parâmetro indexOnBetween é obrigatório quando o tipo é BETWEEN")
    }
    if (type == MoreFiltersType.BETWEEN) {
      return "${fieldName}_${indexOnBetween}_${type.name}"
    }else{
      return "${fieldName}_${type.name}"
    }

  }

  private fun createWheresToDetailedFilters(entity: EntityMetadata, filters: CrudListRequest): Pair<String, Map<String, Any?>?>? {
    return if (!filters.dialogMoreFiltersValue.isNullOrEmpty()) {
      val detailedFilterData = createDetailedFilters(filters)
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

  private fun createDetailedFilters(filters: CrudListRequest): List<DetailedFilterData>{
    val mapFilters = mutableMapOf<String, DetailedFilterData> ()

    filters.dialogMoreFiltersValue?.forEach { (key, value) ->
      val query = key.split("_")
      val fieldName = query.first()
      val type = MoreFiltersType.valueOf(query.last())
      val localValue = MetadataUtils.convertValueByFieldName(fieldName, value)

      val detailedFilterData: DetailedFilterData
      if(type == MoreFiltersType.BETWEEN && mapFilters.containsKey(fieldName)){
        val indexOnBetween = query[1].toInt()
        detailedFilterData = mapFilters[fieldName]!!
        if(indexOnBetween == 0){
          detailedFilterData.value1 = localValue
        }else{
          detailedFilterData.value2 = localValue
        }
      }else{
        detailedFilterData = DetailedFilterData(fieldName, type, localValue, localValue)
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

  fun processConstraintViolationExceptionOnDelete(rootExc: Exception): Exception {
    return rootExc

//    TODO implementar
//    val constraintExc = rootExc.unwrap<ConstraintViolationException>() ?: return rootExc
//
//    try {
//      val constraintName = constraintExc.constraintName
//      defaultMetadataRepository.loadEntityMetadataByFieldName(constraintName).let { entityMetadata ->
//        val msg = "Não é possível excluir o registro pois ele está em uso no 'Cadastro de ${entityMetadata.descr}'"
//        return ValidationException(msg)
//      }
//    }catch (ignore: Exception){
//      //TODO LOGAR a exception ignore
//      return rootExc
//    }
  }
}
