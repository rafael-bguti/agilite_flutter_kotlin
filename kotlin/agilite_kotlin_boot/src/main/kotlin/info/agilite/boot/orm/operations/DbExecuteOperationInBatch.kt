package info.agilite.boot.orm.operations

import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils

class DbExecuteOperationInBatch(
  private val sql: String,
  private val params: List<Map<String, Any?>>,
) : DBChangeOperation<Long> {
  override fun execute(repository: RootRepository): Long {
    val result = repository.jdbc.batchUpdate(
      sql,
      SqlParameterSourceUtils.createBatch(params.toTypedArray())
    )

    return result.size.toLong()
  }
}