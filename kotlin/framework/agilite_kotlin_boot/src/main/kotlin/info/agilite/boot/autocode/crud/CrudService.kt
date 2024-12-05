package info.agilite.boot.autocode.crud

import info.agilite.boot.autocode.crud.models.CrudFilters
import info.agilite.boot.autocode.crud.models.CrudListColumn
import info.agilite.boot.autocode.crud.models.CrudListColumnOptions
import info.agilite.boot.autocode.crud.models.CrudListData
import info.agilite.core.extensions.splitToList
import info.agilite.core.json.JsonUtils
import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.exceptions.AutocompleteMetadataNotFoundException
import info.agilite.boot.jdbcDialect
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.KeyMetadataType
import info.agilite.boot.metadata.models.tasks.TaskCrudMetadata
import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.query.DbQuery
import info.agilite.core.utils.ReflectionUtils
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

interface CrudService<T> {
  fun getCrudMetadata(task: String): TaskCrudMetadata
  fun getCrudListColumns(task: String): List<CrudListColumn>
  fun findListData(task: String, filters: CrudFilters): CrudListData

  fun createNewRecord(task: String): Map<String, Any?>? {
    return null
  }

  fun editRecord(task: String, id: Long): Map<String, Any?>?

  fun convertEntity(task: String, map: Map<String, *>, id: Long?): T
  fun insert(entity: T)
  fun update(entity: T)
  fun delete(task: String, ids: List<Long>)
}

@Service
class DefaultCrudService<T>(
  private val crudRepository: AgiliteCrudRepository
) : CrudService<T> {
  override fun getCrudMetadata(task: String): TaskCrudMetadata {
    val taskName = extractTaskName(task)
    return defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class)
  }

  override fun getCrudListColumns(task: String): List<CrudListColumn> {
    val taskName = extractTaskName(task)
    val columnNames = getColumnNamesToList(taskName)

    return columnNames.map {
      val split = it.split(".")
      val field = defaultMetadataRepository.loadFieldMetadata(split.last())
      val size = if (field.size == 0.0 || field.foreignKeyEntity != null) 50 else field.size.toInt()

      CrudListColumn(
        name = it,
        label = getColumnLabel(split),
        type = field.type.frontEndType,
        options = field.options?.map { opt -> CrudListColumnOptions(opt.value, opt.label) },
        showOnList = field.showInCrudList,
        showOnMoreFilters = field.filterable,
        charCountToWidth = min(max(20, size), 70)
      )
    }
  }

  override fun findListData(task: String, filters: CrudFilters): CrudListData {
    val taskName = extractTaskName(task)
    val query = createListQuery(taskName, filters)

    val data  = crudRepository.findListData(query)
    return data
  }

  override fun convertEntity(task: String, map: Map<String, *>, id: Long?): T {
    val metadata = getCrudMetadata(task).entityMetadata
    val entityClazz = defaultMetadataRepository.loadEntityClass(metadata.name)
    val value = JsonUtils.fromMap(map, entityClazz.java)

    if(id != null){
      ReflectionUtils.setIdValue(value, id)
    }

    return value as T
  }

  override fun insert(entity: T) {
    try {
      val tableName = EntityMappingContext.getTableAndSchema(entity!!::class.java).table
      val entityMetadata = defaultMetadataRepository.loadEntityMetadata(tableName)
      setDefaultValues(entity)

      crudRepository.insert(entity as Any)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnSave(e)
    }
  }

  override fun update(entity: T) {
    try {
      setDefaultValues(entity)

      crudRepository.update(entity as Any)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnSave(e)
    }
  }

  //TODO implementar uma variaável no retorno que indique se o registro é Editavel ou não, assim no cliente quando não for editável, o botão Salvar é desabilitado
  override fun editRecord(task: String, id: Long): Map<String, Any?>? {
    val taskName = extractTaskName(task)
    val query = createEditQuery(taskName, id)

    return crudRepository.findEditData(query)
  }

  override fun delete(task: String, ids: List<Long>) {
    try {
      val taskName = extractTaskName(task)
      val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata
      crudRepository.delete(entityMetadata, ids)
    } catch (e: Exception) {
      throw processConstraintViolationExceptionOnDelete(e)
    }
  }

  protected fun extractTaskName(task: String): String {
    return task.substringAfterLast(".")
  }

  protected fun createEditQuery(taskName: String, id: Long): DbQuery<*> {
    val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata

    val where = WhereSimple(" ${entityMetadata.name}id = :id", mapOf("id" to id))
    val query = DbQueryBuilders.build(
      defaultMetadataRepository.loadEntityClass(entityMetadata.name),
      getColumnNamesToEdit(taskName).joinToString(),
      where = where,
    )

    return query
  }

  protected fun createListQuery(
    taskName: String,
    filters: CrudFilters
  ): DbQuery<*> {
    val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata
    val columnId = "${entityMetadata.name}id as id"

    val whereLikeSearchPair = createWhereToSearch(entityMetadata, filters)
    val whereDetailedFiltersPair = createWheresToDetailedFilters(entityMetadata, filters)
    val wherePadrao = getDefaultWhereOnList(entityMetadata)
    val where = buildString {
      append(" true ")
      AgiliteWhere.defaultWhere(entityMetadata, "AND")
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
      getColumnNamesToList(taskName).joinToString(),
      limitQuery = " LIMIT ${filters.pageSize} OFFSET ${filters.page * filters.pageSize} ",
      columnsProcessor = { "$columnId, $it" },
      where = whereSimple,
      orderBy = { getOrderByToList(entityMetadata) }
    )

    return query
  }

  protected fun getColumnNamesToList(taskName: String): List<String> {
    val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata
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

  protected fun getColumnNamesToEdit(taskName: String): List<String> {
    val entityMetadata = defaultMetadataRepository.loadTaskMetadata(taskName, TaskCrudMetadata::class).entityMetadata
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

  fun getDefaultWhereOnList(entityMetadata: EntityMetadata): Pair<String, Map<String, Any>?>? {
    return null
  }

  private fun createWhereToSearch(entity: EntityMetadata, filters: CrudFilters): Pair<String, Map<String, Any>?>? {
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

  private fun createWheresToDetailedFilters(entity: EntityMetadata, filters: CrudFilters): Pair<String, Map<String, Any?>?>? {
    return if (filters.detailedFilters != null) {
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

  private fun parseRequestFiltersToDetailedFilters(filters: CrudFilters): List<DetailedFilterData>{
    val mapFilters = mutableMapOf<String, DetailedFilterData> ()
    filters.detailedFilters?.forEach { (key, value) ->
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

  fun setDefaultValues(entity: T) {
    //TODO o Código Abaixo está setando a empresa na coluna, porém antes precisa ver se a configuração do usuário/emprsa usa centralização por emrpesa
//    val empresaKey = "${entityMetadata.name}empresa".lowercase();
//    if(entityMetadata.entityHasEmpresaField() && !row.containsKey(empresaKey)){
//      row[empresaKey] = UserContext.currentUser.empId
//    }
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
