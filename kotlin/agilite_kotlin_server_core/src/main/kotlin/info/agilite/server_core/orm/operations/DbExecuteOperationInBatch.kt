package info.agilite.server_core.orm.operations

import info.agilite.server_core.orm.repositories.RootRepository
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