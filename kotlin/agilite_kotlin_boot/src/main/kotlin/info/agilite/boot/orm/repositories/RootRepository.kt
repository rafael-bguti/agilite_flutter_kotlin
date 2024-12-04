package info.agilite.boot.orm.repositories

import info.agilite.core.json.JsonUtils
import info.agilite.boot.jdbcDialect
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.EntityMappingContext
import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.WhereClause
import info.agilite.boot.orm.annotations.EntityCacheable
import info.agilite.boot.orm.cache.DefaultEntityCache
import info.agilite.boot.orm.jdbc.mappers.EntityDataClassRowMapper
import info.agilite.boot.orm.jdbc.mappers.MapRowMapper
import info.agilite.boot.orm.operations.*
import info.agilite.boot.orm.query.DbQuery
import info.agilite.core.utils.ReflectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import kotlin.reflect.KClass

abstract class RootRepository {
  @Autowired lateinit var jdbc: NamedParameterJdbcTemplate

  val entityCache
    get() = DefaultEntityCache

  fun <R: Any> unique(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<R>? = null): R? {
    return try {
      jdbc.queryForObject(query, params, rowMapper ?: EntityDataClassRowMapper(clazz.java))
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }

  fun <R: Any> list(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap(), mapper: RowMapper<R>? = null): List<R> {
    return jdbc.query(query, params, mapper ?: EntityDataClassRowMapper(clazz.java))
  }

  fun <R: Any> unique(query: DbQuery<R>): R? {
    val list = list(query)
    if(list.size > 1) throw Exception("Apenas um resultado era esperado, mas foram encontrados ${list.size}")
    return list.firstOrNull()
  }

  fun <R: Any> list(query: DbQuery<R>): List<R> {
    distinctListMap(query).let { list ->
      if(list.isEmpty()) emptyList<R>()

      return list.map { JsonUtils.fromMap(it, query.clazz.java)  }
    }
  }

  fun distinctMap(query: DbQuery<*>): MutableMap<String, Any?>? {
    distinctListMap(query).let { list ->
      if(list.isEmpty()) return null
      if(list.size > 1) throw Exception("Apenas um resultado era esperado, mas foram encontrados ${list.size}")

      return list.first()
    }
  }

  fun distinctListMap(query: DbQuery<*>): List<MutableMap<String, Any?>> {
    listMap(query.sql(), query.getParams(), MapRowMapper(onlyNonNull = false)).let { list ->
      val oneToManyManager = query.oneToManyManager() ?: return list
      return oneToManyManager.distinct(list)
    }
  }

  fun listMap(query: DbQuery<*>, rowMapper: RowMapper<MutableMap<String, Any?>>? = null): List<MutableMap<String, Any?>> {
    return listMap(query.sql(), query.getParams(), rowMapper ?: MapRowMapper(onlyNonNull = false)).let {
      if(it.isEmpty()) emptyList<MutableMap<String, Any?>>()

      val oneToManyManager = query.oneToManyManager() ?: return it
      oneToManyManager.distinct(it)
    }
  }

  fun <R: Any> uniqueSingleColumn(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap()): R? {
    return try {
      jdbc.queryForObject(query, params, clazz.java)
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }
  fun <R: Any> listSingleColumn(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap()): List<R> {
    return jdbc.queryForList(query, params, clazz.java)
  }

  final fun uniqueMap(query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<MutableMap<String, Any?>>? = null): MutableMap<String, Any?>? {
    return try {
      jdbc.queryForObject(query, params, rowMapper ?: MapRowMapper())
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }
  final fun listMap(query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<MutableMap<String, Any?>>? = null): List<MutableMap<String, Any?>> {
    return jdbc.query(query, params, rowMapper ?: MapRowMapper())
  }

  fun execute(query: String, params: Map<String, Any?> = emptyMap()): Int {
    return jdbc.update(query, params)
  }

  fun delete(tableName: String, id: Long, schema: String? = null): Int {
    return delete(tableName, listOf(id), schema)
  }
  fun delete(tableName: String, ids: List<Long>, schema: String? = null): Int {
    return DbDeleteOperation(tableName, ids, schema).execute(this)
  }

  fun save(entity: Any) {
    if(entity is Map<*, *>)   {
      throw Exception("Não é permitido utilizar um Map no método insertOrUpdate por entity, utilize o método que recebe um Map")
    }
    val tableNameAnsSchemaName = EntityMappingContext.getTableAndSchema(entity::class.java)
    val isNew = ReflectionUtils.entityIsNew(entity, tableNameAnsSchemaName.table)
    if(isNew){
      insert(entity)
    }else {
      update(entity)
    }
  }

  fun save(tableName: String, values: Map<String, Any?>, schema: String? = null) {
    if(values["${tableName.lowercase()}id"] != null){
      update(tableName, values, schema)
    }else {
      insert(tableName, values, schema)
    }
  }
  fun insert(entity: Any) {
    if(entity is Map<*, *>)   {
      throw Exception("Não é permitido utilizar um Map no método insert por entity, utilize o método que recebe um Map")
    }

    DbInsertOperationFromEntity(entity).execute(this)
  }
  fun insert(tableName: String, values: Map<String, Any?>, schema: String? = null): Long {
    return DbInsertOperationFromMap(tableName, values, schema).execute(this)
  }
  fun update(entity: Any): Int {
    if(entity is Map<*, *>)   {
      throw Exception("Não é permitido utilizar um Map no método update por entity, utilize o método que recebe um Map")
    }

    return DbUpdateOperationFromEntity(entity).execute(this)
  }
  fun update(tableName: String, values: Map<String, Any?>, schema: String? = null ): Int {
    return DbUpdateOperationFromMap(tableName, values, schema).execute(this)
  }
  fun updateChanges(entity: AbstractEntity): Int {
    return DbUpdateChangesOperationFromEntity(entity).execute(this)
  }

  fun nextIds(tableName: String, qtdIds: Int = 1): IdsList {
    val seqName = jdbcDialect.sequenceName(tableName)
    return if(qtdIds == 1){
      IdsList(mutableListOf(uniqueSingleColumn(Long::class,"SELECT nextval('$seqName')")!!))
    }else{
      IdsList(listSingleColumn(Long::class,"SELECT nextval('$seqName') FROM generate_series(1, $qtdIds)").toMutableList())
    }
  }

  internal val jdbcTemplate get() = jdbc.jdbcTemplate

  //------- Métodos úteis --------
  fun <R: Any> findById(clazz: KClass<R>, idValue: Long, rowMapper: RowMapper<R>? = null, cacheable: Boolean? = null): R? {
    val useCache = cacheable ?: isCacheable(clazz)
    if(useCache){
      val tableAndDefaultSchema = EntityMappingContext.getTableAndSchema(clazz.java)
      return entityCache.getOrPut(tableAndDefaultSchema.table, idValue.toString()) {
        executeFindById(clazz, idValue, rowMapper)
      }
    }else{
      return executeFindById(clazz, idValue, rowMapper)
    }
  }

  fun <R: Any> uniqueByWhere(clazz: KClass<R>, where: WhereClause, rowMapper: RowMapper<R>? = null): R? {
    val columns = EntityMappingContext.getColumnsQuery(clazz.java).joinToString()
    val dbQuery = DbQuery(clazz, columns, where = where)

    return unique(dbQuery)
  }

  fun <R: Any> listByWhere(clazz: KClass<R>, where: WhereClause, simpleJoin: String? = null,  rowMapper: RowMapper<R>? = null): List<R> {
    val columns = EntityMappingContext.getColumnsQuery(clazz.java).joinToString()
    val dbQuery = DbQuery(
      clazz,
      columns,
      where = where,
      simpleJoin = simpleJoin
    )

    return list(dbQuery)
  }

  fun <R: Any> uniqueSingleColumnById(clazz: KClass<R>, column: String, id: Long): R? {
    val schemaAndTable = EntityMappingContext.getTableAndSchema(clazz.java)
    val sql = "SELECT $column FROM ${schemaAndTable.sql} WHERE ${schemaAndTable.table}id = $id"
    return uniqueSingleColumn(clazz, sql, emptyMap())
  }

  fun inflate(entity: AbstractEntity, joins: String? = null) {
    EntityInflator(this).inflate(entity, joins)
  }

  //Utils internos
  fun <R: Any> executeFindById(clazz: KClass<R>, idValue: Long, rowMapper: RowMapper<R>? = null): R? {
    val tableAndDefaultSchema = EntityMappingContext.getTableAndSchema(clazz.java)
    val idColumnName = "${tableAndDefaultSchema.table}id"
    val columns = EntityMappingContext.getColumnsQuery(clazz.java).joinToString()
    val dbQuery = DbQuery(clazz, columns, where = WhereAllEquals(mapOf(idColumnName to idValue)))

    return unique(dbQuery)
  }

  private val classIsCacheable = mutableMapOf<KClass<*>, Boolean>()
  fun isCacheable(clazz: KClass<*>): Boolean {
    return classIsCacheable.getOrPut(clazz) {
      clazz.java.isAnnotationPresent(EntityCacheable::class.java)
    }
  }
}
