package info.agilite.boot.orm.repositories

import info.agilite.boot.jdbcDialect
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.orm.*
import info.agilite.boot.orm.annotations.EntityCacheable
import info.agilite.boot.orm.cache.DefaultEntityCache
import info.agilite.boot.orm.jdbc.mappers.EntityDataClassRowMapper
import info.agilite.boot.orm.jdbc.mappers.MapRowMapper
import info.agilite.boot.orm.operations.*
import info.agilite.boot.orm.query.DbQuery
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.ReflectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import kotlin.reflect.KClass

@Repository
abstract class RootRepository {
  @Autowired lateinit var jdbc: JdbcConnection

  fun <R: Any> unique(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<R>? = null): R? {
    return try {
      jdbc.queryForObject(query, params, rowMapper ?: EntityDataClassRowMapper(clazz.java))
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }
  fun <R: Any> uniqueSingleColumn(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap()): R? {
    return try {
      jdbc.queryForObject(query, params, clazz.java)
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }
  fun <R: Any> unique(query: DbQuery<R>): R? {
    val list = list(query)
    if(list.size > 1) throw Exception("Apenas um resultado era esperado, mas foram encontrados ${list.size}")
    return list.firstOrNull()
  }

  final fun uniqueMap(query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<MutableMap<String, Any?>>? = null): MutableMap<String, Any?>? {
    return try {
      jdbc.queryForObject(query, params, rowMapper ?: MapRowMapper())
    }catch(e: EmptyResultDataAccessException){
      null
    }
  }


  fun <R: Any> list(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap(), mapper: RowMapper<R>? = null): List<R> {
    return jdbc.query(query, params, mapper ?: EntityDataClassRowMapper(clazz.java))
  }
  protected fun <R: Any> list(query: DbQuery<R>, clazz: Class<R>? = null ): List<R> {
    distinctListMap(query).let { list ->
      if(list.isEmpty()) emptyList<R>()

      return list.map { JsonUtils.fromMap(it, clazz ?: query.clazz.java)  }
    }
  }
  fun listMap(query: DbQuery<*>, rowMapper: RowMapper<MutableMap<String, Any?>>? = null): List<MutableMap<String, Any?>> {
    return listMap(query.sql(), query.getParams(), rowMapper ?: MapRowMapper(onlyNonNull = false)).let {
      if(it.isEmpty()) emptyList<MutableMap<String, Any?>>()

      val oneToManyManager = query.oneToManyManager() ?: return it
      oneToManyManager.distinct(it)
    }
  }

  final fun listMap(query: String, params: Map<String, Any?> = emptyMap(), rowMapper: RowMapper<MutableMap<String, Any?>>? = null): List<MutableMap<String, Any?>> {
    return jdbc.query(query, params, rowMapper ?: MapRowMapper())
  }

  fun <R: Any> listSingleColumn(clazz: KClass<R>, query: String, params: Map<String, Any?> = emptyMap()): List<R> {
    return jdbc.queryForList(query, params, clazz.java)
  }



  fun distinctMap(query: DbQuery<*>): MutableMap<String, Any?>? {
    distinctListMap(query).let { list ->
      if(list.isEmpty()) return null
      if(list.size > 1) throw Exception("Apenas um resultado era esperado, mas foram encontrados ${list.size}")

      return list.first()
    }
  }
  fun distinctListMap(query: DbQuery<*>): List<MutableMap<String, Any?>> {
    listMap(query.sql(), query.getParams(), MapRowMapper(onlyNonNull = false, nested = true)).let { list ->
      val oneToManyManager = query.oneToManyManager() ?: return list
      return oneToManyManager.distinct(list)
    }
  }

  fun execute(query: String, params: Map<String, Any?> = emptyMap()): Int {
    return jdbc.update(query, params)
  }

  fun executeBatch(batchOperations: BatchOperations){
    batchOperations.execute(this)
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

  fun ignoreNextTransactionValidation(){
    jdbc.ignoreNextTransaction()
  }

  fun nextIds(tableName: String, qtdIds: Int = 1): IdsList {
    val seqName = jdbcDialect.sequenceName(tableName)
    return if(qtdIds == 1){
      IdsList(mutableListOf(uniqueSingleColumn(Long::class,"SELECT nextval('$seqName')")!!))
    }else{
      IdsList(listSingleColumn(Long::class,"SELECT nextval('$seqName') FROM generate_series(1, $qtdIds)").toMutableList())
    }
  }

  //------- Métodos úteis --------
  fun <R: Any> findById(clazz: KClass<R>, idValue: Long, rowMapper: RowMapper<R>? = null, cacheable: Boolean? = null): R? {
    val useCache = (cacheable ?: isCacheable(clazz)) && AbstractEntity::class.java.isAssignableFrom(clazz.java)
    if(useCache){
      val tableAndDefaultSchema = EntityMappingContext.getTableAndSchema(clazz.java)
      return DefaultEntityCache.getOrPut(tableAndDefaultSchema.table, idValue) {
        executeFindById(clazz, idValue, rowMapper) as AbstractEntity?
      } as R?
    }else{
      return executeFindById(clazz, idValue, rowMapper)
    }
  }

  fun <R: Any> uniqueByWhere(clazz: KClass<R>, where: Where, rowMapper: RowMapper<R>? = null): R? {
    val columns = EntityMappingContext.getColumnsQuery(clazz.java).joinToString()
    val dbQuery = DbQuery(clazz, columns, where = where)

    return unique(dbQuery)
  }

  fun <R: Any> listByWhere(clazz: KClass<R>, where: Where, simpleJoin: String? = null, rowMapper: RowMapper<R>? = null): List<R> {
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

  fun defaultWhere(entityMetadata: EntityMetadata): String {
    return AgiliteWhere.defaultWhere(entityMetadata)
  }


  fun setTenant(tenant: String) {
    jdbc.setTenant(tenant)
  }

  /***
   * Carrega as propriedades de um objeto que não foram carregadas.
   * É possível carregar os ManyToOne em qualquer nível, porém OneToMany só é possível carregar o primeiro nível.
   * Exemplo supondo que se tenha carregado apenas o srf01id, srf01tipo, srf01nome
   * Ao chamar:
   *  1. repo.inflate(srf01) - vai carregar todas as propriedades do Srf01 e vai carregar os ManyToOne e OneToMany do srf01 apenas com ID
   *  2. repo.inflate(srf01, "srf012") - igual ao anterior, mas vai carregar o OneToMany srf012 com todas as propriedades e as Fk do Srf012 apenas com Id
   *  3. repo.inflate(srf01, "srf01natureza") - igual ao 1., mas vai carregar todas as propriedades do ManyToOne srf01natureza
   *  4. repo.inflate(srf01, "srf012.srf012forma") - vai dar erro, pois não pode inflar ForeingKeys de um OneToMany
   */
  fun inflate(entity: AbstractEntity, joins: String? = null) {
    EntityInflator(this).inflate(entity, joins)
  }

  //Utils internos
  fun <R: Any> executeFindById(clazz: KClass<R>, idValue: Long, rowMapper: RowMapper<R>? = null): R? {
    val tableAndDefaultSchema = EntityMappingContext.getTableAndSchema(clazz.java)
    val idColumnName = "${tableAndDefaultSchema.table}id"
    val columns = EntityMappingContext.getColumnsQuery(clazz.java).joinToString()
    val dbQuery = DbQuery(clazz, columns, where = WhereEquals(idColumnName, idValue))

    return unique(dbQuery)
  }

  private val classIsCacheable = mutableMapOf<KClass<*>, Boolean>()
  fun isCacheable(clazz: KClass<*>): Boolean {
    return classIsCacheable.getOrPut(clazz) {
      clazz.java.isAnnotationPresent(EntityCacheable::class.java)
    }
  }
}
