package info.agilite.server_core.orm

import info.agilite.shared.extensions.doInLock
import info.agilite.shared.extensions.nullIfEmpty
import info.agilite.server_core.defaultMetadataRepository
import info.agilite.server_core.metadata.exceptions.EntityMetadataNotFoundException
import info.agilite.server_core.orm.annotations.DbTable
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock

object EntityMappingContext {
  private val lock = ReentrantReadWriteLock()
  private val read: Lock = lock.readLock()
  private val write: Lock = lock.writeLock()

  private val TABLES_CACHE = mutableMapOf<Class<*>, TableAndDefaultSchema>()
  private val COLUMNS_CACHE = mutableMapOf<Class<*>, List<String>>()

  internal fun getTableAndSchema(clazz: Class<*>): TableAndDefaultSchema {
    return extractTableAndSchemaMapping(clazz)
  }

  fun getColumnsQuery(clazz: Class<*>): List<String> {
    val columns = read.doInLock {
      COLUMNS_CACHE[clazz]
    }

    return columns ?: write.doInLock {
      val columns = try {
        defaultMetadataRepository.loadEntityMetadata(clazz.simpleName).fields.map {
          if(it.foreignKeyEntity != null){
            "${it.name}.${it.foreignKeyEntity.lowercase()}id"
          } else {
            it.name
          }
        }
      } catch (e: EntityMetadataNotFoundException) {
        clazz.declaredFields.map {
          if(it.type.isAssignableFrom(AbstractEntity::class.java)){
            "${it.name}.${it.type.simpleName.lowercase()}id"
          } else{
            it.name
          }
        }
      }

      COLUMNS_CACHE[clazz] = columns
      columns
    }
  }

  internal fun getTableAndSchemaQuery(clazz: Class<*>): String {
    return extractTableAndSchemaMapping(clazz).sql
  }

  private fun extractTableAndSchemaMapping(clazz: Class<*>): TableAndDefaultSchema {
    val tableAndSchema = read.doInLock {
      TABLES_CACHE[clazz]
    }

    return tableAndSchema ?: write.doInLock {
      val table = clazz.getAnnotation(DbTable::class.java)
      val value: TableAndDefaultSchema =
        if (table != null) {
          TableAndDefaultSchema(table.name, table.schema.nullIfEmpty())
        } else {
          TableAndDefaultSchema(clazz.simpleName, null)
        }

      TABLES_CACHE[clazz] = value
      value
    }
  }
}
