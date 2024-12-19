package info.agilite.boot.orm

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.PreparedStatementCallback
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowCallbackHandler
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.KeyHolder
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.stream.Stream

@Component
class JdbcConnection(
  private val jdbc: NamedParameterJdbcTemplate,
) {
  private val LOGGER = LoggerFactory.getLogger(JdbcConnection::class.java)

  val jdbcTemplate = jdbc.jdbcTemplate

  fun <T> execute(sql: String, paramSource: SqlParameterSource, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    return jdbc.execute(sql, paramSource, action)
  }

  fun <T> execute(sql: String, paramMap: Map<String, *>, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    return jdbc.execute(sql, paramMap, action)
  }

  fun <T> execute(sql: String, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    return jdbc.execute(sql, action)
  }

  fun <T> query(sql: String, paramSource: SqlParameterSource, rse: ResultSetExtractor<T>): T? {
    logarSQL(sql)
    return jdbc.query(sql, paramSource, rse)
  }

  fun <T> query(sql: String, paramMap: Map<String, *>, rse: ResultSetExtractor<T>): T? {
    logarSQL(sql)
    return jdbc.query(sql, paramMap, rse)
  }

  fun <T> query(sql: String, rse: ResultSetExtractor<T>): T? {
    logarSQL(sql)
    return jdbc.query(sql, rse)
  }

  fun query(sql: String, paramSource: SqlParameterSource, rch: RowCallbackHandler) {
    logarSQL(sql)
    jdbc.query(sql, paramSource, rch)
  }

  fun query(sql: String, paramMap: Map<String, *>, rch: RowCallbackHandler) {
    logarSQL(sql)
    jdbc.query(sql, paramMap, rch)
  }

  fun query(sql: String, rch: RowCallbackHandler) {
    logarSQL(sql)
    jdbc.query(sql, rch)
  }

  fun <T> query(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): List<T> {
    logarSQL(sql)
    return jdbc.query(sql, paramSource, rowMapper)
  }

  fun <T> query(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): List<T> {
    logarSQL(sql)
    return jdbc.query(sql, paramMap, rowMapper)
  }

  fun <T> query(sql: String, rowMapper: RowMapper<T>): List<T> {
    logarSQL(sql)
    return jdbc.query(sql, rowMapper)
  }

  fun <T> queryForStream(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): Stream<T> {
    return jdbc.queryForStream(sql, paramSource, rowMapper)
  }

  fun <T> queryForStream(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): Stream<T> {
    return jdbc.queryForStream(sql, paramMap, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): T? {
    return jdbc.queryForObject(sql, paramSource, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): T? {
    logarSQL(sql)
    return jdbc.queryForObject(sql, paramMap, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramSource: SqlParameterSource, requiredType: Class<T>): T? {
    return jdbc.queryForObject(sql, paramSource, requiredType)
  }

  fun <T> queryForObject(sql: String, paramMap: Map<String, *>, requiredType: Class<T>): T? {
    logarSQL(sql)
    return jdbc.queryForObject(sql, paramMap, requiredType)
  }

  fun queryForMap(sql: String, paramSource: SqlParameterSource): Map<String, Any> {
    return jdbc.queryForMap(sql, paramSource)
  }

  fun queryForMap(sql: String, paramMap: Map<String, *>): Map<String, Any> {
    return jdbc.queryForMap(sql, paramMap)
  }

  fun <T> queryForList(sql: String, paramSource: SqlParameterSource, elementType: Class<T>): List<T> {
    return jdbc.queryForList(sql, paramSource, elementType)
  }

  fun <T> queryForList(sql: String, paramMap: Map<String, *>, elementType: Class<T>): List<T> {
    logarSQL(sql)
    return jdbc.queryForList(sql, paramMap, elementType)
  }

  fun queryForList(sql: String, paramSource: SqlParameterSource): List<Map<String, Any>> {
    return jdbc.queryForList(sql, paramSource)
  }

  fun queryForList(sql: String, paramMap: Map<String, *>): List<Map<String, Any>> {
    return jdbc.queryForList(sql, paramMap)
  }

  fun queryForRowSet(sql: String, paramSource: SqlParameterSource): SqlRowSet {
    return jdbc.queryForRowSet(sql, paramSource)
  }

  fun queryForRowSet(sql: String, paramMap: Map<String, *>): SqlRowSet {
    return jdbc.queryForRowSet(sql, paramMap)
  }

  fun update(sql: String, paramSource: SqlParameterSource): Int {
    validarTransacaoAberta()
    return jdbc.update(sql, paramSource)
  }

  fun update(sql: String, paramMap: Map<String, *>): Int {
    validarTransacaoAberta()
    return jdbc.update(sql, paramMap)
  }

  internal fun updateIgnoreTransaction(sql: String, paramMap: Map<String, *>): Int {
    return jdbc.update(sql, paramMap)
  }

  fun update(sql: String, paramSource: SqlParameterSource, generatedKeyHolder: KeyHolder): Int {
    validarTransacaoAberta()
    return jdbc.update(sql, paramSource, generatedKeyHolder)
  }

  fun update(sql: String, paramSource: SqlParameterSource, generatedKeyHolder: KeyHolder, keyColumnNames: Array<String>?): Int {
    validarTransacaoAberta()
    return jdbc.update(sql, paramSource, generatedKeyHolder, keyColumnNames)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>): IntArray {
    validarTransacaoAberta()
    return jdbc.batchUpdate(sql, batchArgs)
  }

  fun batchUpdate(sql: String, batchValues: Array<Map<String, *>>): IntArray {
    validarTransacaoAberta()
    return jdbc.batchUpdate(sql, batchValues)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>, generatedKeyHolder: KeyHolder): IntArray {
    validarTransacaoAberta()
    return jdbc.batchUpdate(sql, batchArgs, generatedKeyHolder)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>, generatedKeyHolder: KeyHolder, keyColumnNames: Array<String>?): IntArray {
    validarTransacaoAberta()
    return jdbc.batchUpdate(sql, batchArgs, generatedKeyHolder, keyColumnNames)
  }

  private fun logarSQL(sql: String) {
    if(LOGGER.isDebugEnabled){
      val stack = Thread.currentThread().stackTrace.filter {
        it.className.startsWith("info.agilite")
      }.map {
        "Chamada de: ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"
      }.joinToString("\n" )

      LOGGER.debug("""
      |
      | 
      |  
      |+     ************************** SQL **************************
      |+       ${sql.replace("\n", "\n+       ")}
      |+     --- Stack ---
      |+       ${stack.replace("\n", "\n+       ")}
      |+     ************************** END **************************
      |
      |
      """.trimMargin())
    }
  }

  internal fun validarTransacaoAberta() {
    if(!TransactionSynchronizationManager.isActualTransactionActive()){
      throw Exception("Transação não está aberta")
    }
  }

}