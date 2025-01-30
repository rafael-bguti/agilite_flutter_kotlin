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
  private val currentTransactionIgnore = ThreadLocal<Boolean>()
  fun <T> execute(sql: String, paramSource: SqlParameterSource, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    logarExecuteDebug("execute", sql)
    return jdbc.execute(sql, paramSource, action)
  }

  fun <T> execute(sql: String, paramMap: Map<String, *>, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    logarExecuteDebug("execute", sql)
    return jdbc.execute(sql, paramMap, action)
  }

  fun <T> execute(sql: String, action: PreparedStatementCallback<T>): T? {
    validarTransacaoAberta()
    logarExecuteDebug("execute", sql)
    return jdbc.execute(sql, action)
  }

  fun <T> query(sql: String, paramSource: SqlParameterSource, rse: ResultSetExtractor<T>): T? {
    logarQueryDebug(sql)
    return jdbc.query(sql, paramSource, rse)
  }

  fun <T> query(sql: String, paramMap: Map<String, *>, rse: ResultSetExtractor<T>): T? {
    logarQueryDebug(sql, paramMap)
    return jdbc.query(sql, paramMap, rse)
  }

  fun <T> query(sql: String, rse: ResultSetExtractor<T>): T? {
    logarQueryDebug(sql)
    return jdbc.query(sql, rse)
  }

  fun query(sql: String, paramSource: SqlParameterSource, rch: RowCallbackHandler) {
    logarQueryDebug(sql)
    jdbc.query(sql, paramSource, rch)
  }

  fun query(sql: String, paramMap: Map<String, *>, rch: RowCallbackHandler) {
    logarQueryDebug(sql, paramMap)
    jdbc.query(sql, paramMap, rch)
  }

  fun query(sql: String, rch: RowCallbackHandler) {
    logarQueryDebug(sql)
    jdbc.query(sql, rch)
  }

  fun <T> query(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): List<T> {
    logarQueryDebug(sql)
    return jdbc.query(sql, paramSource, rowMapper)
  }

  fun <T> query(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): List<T> {
    logarQueryDebug(sql, paramMap)
    return jdbc.query(sql, paramMap, rowMapper)
  }

  fun <T> query(sql: String, rowMapper: RowMapper<T>): List<T> {
    logarQueryDebug(sql)
    return jdbc.query(sql, rowMapper)
  }

  fun <T> queryForStream(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): Stream<T> {
    logarQueryDebug(sql)
    return jdbc.queryForStream(sql, paramSource, rowMapper)
  }

  fun <T> queryForStream(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): Stream<T> {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForStream(sql, paramMap, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramSource: SqlParameterSource, rowMapper: RowMapper<T>): T? {
    logarQueryDebug(sql)
    return jdbc.queryForObject(sql, paramSource, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramMap: Map<String, *>, rowMapper: RowMapper<T>): T? {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForObject(sql, paramMap, rowMapper)
  }

  fun <T> queryForObject(sql: String, paramSource: SqlParameterSource, requiredType: Class<T>): T? {
    logarQueryDebug(sql)
    return jdbc.queryForObject(sql, paramSource, requiredType)
  }

  fun <T> queryForObject(sql: String, paramMap: Map<String, *>, requiredType: Class<T>): T? {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForObject(sql, paramMap, requiredType)
  }

  fun queryForMap(sql: String, paramSource: SqlParameterSource): Map<String, Any> {
    logarQueryDebug(sql)
    return jdbc.queryForMap(sql, paramSource)
  }

  fun queryForMap(sql: String, paramMap: Map<String, *>): Map<String, Any> {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForMap(sql, paramMap)
  }

  fun <T> queryForList(sql: String, paramSource: SqlParameterSource, elementType: Class<T>): List<T> {
    logarQueryDebug(sql)
    return jdbc.queryForList(sql, paramSource, elementType)
  }

  fun <T> queryForList(sql: String, paramMap: Map<String, *>, elementType: Class<T>): List<T> {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForList(sql, paramMap, elementType)
  }

  fun queryForList(sql: String, paramSource: SqlParameterSource): List<Map<String, Any>> {
    logarQueryDebug(sql)
    return jdbc.queryForList(sql, paramSource)
  }

  fun queryForList(sql: String, paramMap: Map<String, *>): List<Map<String, Any>> {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForList(sql, paramMap)
  }

  fun queryForRowSet(sql: String, paramSource: SqlParameterSource): SqlRowSet {
    logarQueryDebug(sql)
    return jdbc.queryForRowSet(sql, paramSource)
  }

  fun queryForRowSet(sql: String, paramMap: Map<String, *>): SqlRowSet {
    logarQueryDebug(sql, paramMap)
    return jdbc.queryForRowSet(sql, paramMap)
  }

  fun update(sql: String, paramSource: SqlParameterSource): Int {
    validarTransacaoAberta()
    logarExecuteDebug("update", sql)
    return jdbc.update(sql, paramSource)
  }

  fun update(sql: String, paramMap: Map<String, *>): Int {
    validarTransacaoAberta()
    logarExecuteDebug("update", sql)
    return jdbc.update(sql, paramMap)
  }



  internal fun updateIgnoreTransaction(sql: String, paramMap: Map<String, *>): Int {
    logarExecuteDebug("update", sql)
    return jdbc.update(sql, paramMap)
  }

  fun update(sql: String, paramSource: SqlParameterSource, generatedKeyHolder: KeyHolder): Int {
    validarTransacaoAberta()
    logarExecuteDebug("update", sql)
    return jdbc.update(sql, paramSource, generatedKeyHolder)
  }

  fun update(sql: String, paramSource: SqlParameterSource, generatedKeyHolder: KeyHolder, keyColumnNames: Array<String>?): Int {
    validarTransacaoAberta()
    logarExecuteDebug("update", sql)
    return jdbc.update(sql, paramSource, generatedKeyHolder, keyColumnNames)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>): IntArray {
    validarTransacaoAberta()
    logarExecuteDebug("batchUpdate", sql)
    return jdbc.batchUpdate(sql, batchArgs)
  }

  fun batchUpdate(sql: String, batchValues: Array<Map<String, *>>): IntArray {
    validarTransacaoAberta()
    logarExecuteDebug("batchUpdate", sql)
    return jdbc.batchUpdate(sql, batchValues)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>, generatedKeyHolder: KeyHolder): IntArray {
    validarTransacaoAberta()
    logarExecuteDebug("batchUpdate", sql)
    return jdbc.batchUpdate(sql, batchArgs, generatedKeyHolder)
  }

  fun batchUpdate(sql: String, batchArgs: Array<SqlParameterSource>, generatedKeyHolder: KeyHolder, keyColumnNames: Array<String>?): IntArray {
    validarTransacaoAberta()
    logarExecuteDebug("batchUpdate", sql)
    return jdbc.batchUpdate(sql, batchArgs, generatedKeyHolder, keyColumnNames)
  }

  private fun logarExecuteDebug(operation: String, sql: String) {
    if(LOGGER.isDebugEnabled){
      LOGGER.debug("\n$operation - $sql")
    }
  }
  private fun logarQueryDebug(sql: String, paramMap: Map<String, *>? = null) {
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
      |       ${sql.replace("\n", "\n       ")}
      |+     --- Params ---
      |+       ${paramMap?.map { "${it.key} = ${it.value}" }?.joinToString(", ")}
      |+     --- Stack ---
      |+       ${stack.replace("\n", "\n+       ")}
      |+     ************************** END **************************
      |
      |
      """.trimMargin())
    }
  }

  fun ignoreNextTransaction(){
    currentTransactionIgnore.set(true)
  }
  internal fun validarTransacaoAberta() {
    if(currentTransactionIgnore.get() == true){
      currentTransactionIgnore.set(false)
      return
    }

    if(!TransactionSynchronizationManager.isActualTransactionActive()){
      throw Exception("Transação não está aberta")
    }
  }

}