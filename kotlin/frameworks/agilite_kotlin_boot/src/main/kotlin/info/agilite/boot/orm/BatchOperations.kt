package info.agilite.boot.orm

import info.agilite.boot.orm.operations.DbExecuteOperationInBatch
import info.agilite.boot.orm.operations.DbInsertOperationInBatch
import info.agilite.boot.orm.operations.DbUpdateOperationInBatch
import info.agilite.boot.orm.repositories.RootRepository
import kotlin.reflect.KClass

/**
 * Atenção o Batch tem algumas limitações:
 * 1. Ele não executa as operações cascades de Insert e Update de listas filhas da entidade
 * 2. Ele não seta os ids gerados no banco de dados nas entidades inseridas
 */

open class BatchOperations(
  private val operations: MutableList<Operation> = mutableListOf(),
  private val customOperations: MutableList<CustomOperation> = mutableListOf(),
) {
  fun insert(entity: Any, executionOrder: Int? = null) {
    operations.add(Operation(entity::class, OperationType.INSERT, entity, executionOrder ?: operations.size))
  }

  fun update(entity: Any, executionOrder: Int? = null) {
    operations.add(Operation(entity::class, OperationType.UPDATE, entity, executionOrder ?: operations.size))
  }

  fun customOperation(sql: String, params: Map<String, Any?>, executionOrder: Int? = null) {
    customOperations.add(CustomOperation(sql, params, executionOrder ?: customOperations.size))
  }

  fun execute(dao: RootRepository) {
    val operationsList = createOperationList()

    operationsList.forEach { operationsFromClass ->
      if (operationsFromClass.insertList.isNotEmpty()) {
        DbInsertOperationInBatch(operationsFromClass.insertList).execute(dao)
      }

      if (operationsFromClass.updateList.isNotEmpty()) {
        DbUpdateOperationInBatch(operationsFromClass.updateList).execute(dao)
      }
    }

    val customOperationsList = createCustomOperationList()
    customOperationsList.forEach {
      DbExecuteOperationInBatch(it.sql, it.params).execute(dao)
    }

    operations.clear()
  }

  private fun createOperationList(): MutableList<OperationsFromClass> {
    operations.sortBy { it.order }

    val operationsList = mutableListOf<OperationsFromClass>()
    val classWithOperationsAdded = mutableSetOf<KClass<*>>()
    operations.forEach { operation ->
      if (!classWithOperationsAdded.contains(operation.clazz)) {
        operationsList.add(OperationsFromClass(operation.clazz))
        operationsList.last().addOperation(operation)
        classWithOperationsAdded.add(operation.clazz)
      } else {
        operationsList.find { operationList -> operation.clazz == operationList.clazz }?.addOperation(operation)
      }
    }
    return operationsList
  }

  private fun createCustomOperationList(): List<CustomOperationFromSQL> {
    val mapOfSql = mutableMapOf<String, CustomOperationFromSQL>()
    customOperations.forEach { operation ->
      if (!mapOfSql.containsKey(operation.sql)) {
        mapOfSql[operation.sql] = CustomOperationFromSQL(operation.order, operation.sql)
      }
      mapOfSql[operation.sql]!!.params.add(operation.params)
    }

    return mapOfSql.values.sortedBy { it.order }.toList()
  }
}

private data class CustomOperationFromSQL(
  val order: Int,
  val sql: String,
  val params: MutableList<Map<String, Any?>> = mutableListOf(),
)

private data class OperationsFromClass(
  val clazz: KClass<*>,
  val insertList: MutableList<Any> = mutableListOf(),
  val updateList: MutableList<Any> = mutableListOf()
) {
  fun addOperation(operation: Operation) {
    if (operation.type == OperationType.INSERT) {
      insertList.add(operation.entity)
    } else {
      updateList.add(operation.entity)
    }
  }
}

data class CustomOperation(
  val sql: String,
  val params: Map<String, Any?>,
  val order: Int
)

data class Operation(
  val clazz: KClass<*>,
  val type: OperationType,
  val entity: Any,
  val order: Int
)

enum class OperationType {
  INSERT,
  UPDATE,
  CUSTOM
}