package info.agilite.server_core.orm.operations

import info.agilite.server_core.orm.cache.DefaultEntityCache
import info.agilite.server_core.orm.repositories.RootRepository

internal class DbDeleteOperation(
  private val tableName: String,
  private val ids: List<Long>,
  private val schema: String? = null,
) : DBChangeOperation<Int> {
  override fun execute(repository: RootRepository): Int {
    val localSchema = schema?.let { "$it." } ?: ""
    val result = repository.execute("DELETE FROM $localSchema$tableName WHERE ${tableName}id IN (:ids)", mapOf("ids" to ids))

    ids.map{it.toString()}.forEach { DefaultEntityCache.invalidate(tableName, it) }
    return result
  }
}