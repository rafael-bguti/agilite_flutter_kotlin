package info.agilite.boot.orm.query

import info.agilite.core.extensions.splitToList
import info.agilite.boot.orm.Where
import kotlin.reflect.KClass



class DbQueryBuilders {
  companion object {
    fun <T: Any> build(
      clazz: KClass<T>,
      columns: String = "*",
      simpleJoin: String? = null,
      where: Where? = null,
      scraps: String? = null,
      orderBy: (() -> String)? = null,
      limitQuery: String? = null,
      columnsProcessor: ((columns: String) -> String)? = null,
    ): DbQuery<T> {
      val columnList = columns.splitToList()

      val joinBuilders = mutableMapOf<String, InternalJoinBuilder>()
      val dbQueryColumns = mutableListOf<String>()
      for(column in columnList) {
        if(!column.contains(".")){
          dbQueryColumns.add(column)
        }else{
          val split = column.split(".")
          var lastJoinBuilder: InternalJoinBuilder? = null
          for(i in 0  ..< split.size-1){
            lastJoinBuilder = loadOrCreateJoinBuilder(lastJoinBuilder?.subJoinBuilders ?: joinBuilders, split[i])
          }
          lastJoinBuilder!!.columns.add(split.last())
        }
      }

      return DbQuery(
        clazz,
        dbQueryColumns.joinToString(),
        joinBuilders.values.map { it.build() },
        simpleJoin = simpleJoin,
        where = where,
        scraps = scraps,
        orderBy = orderBy,
        limitQuery = limitQuery,
        columnsProcessor = columnsProcessor
      )
    }

    private fun loadOrCreateJoinBuilder(joinBuilders: MutableMap<String, InternalJoinBuilder>, column: String): InternalJoinBuilder {
      return joinBuilders.getOrPut(column) { InternalJoinBuilder(column) }
    }
  }
}
private class InternalJoinBuilder(
  val parentFkColumn: String,
){
  val columns = mutableListOf<String>()
  val subJoinBuilders = mutableMapOf<String, InternalJoinBuilder>()

  fun build(): DbJoin {
    val localColumns = if(columns.size == 1 && columns.first() == "*") null else columns.joinToString()

    val join = DbJoin(parentFkColumn, localColumns)
    for(subJoin in subJoinBuilders.values){
      join.addJoin(subJoin.build())
    }
    return join
  }

}
