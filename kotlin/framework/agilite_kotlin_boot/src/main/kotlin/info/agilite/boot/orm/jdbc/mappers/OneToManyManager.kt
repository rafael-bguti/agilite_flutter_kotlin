package info.agilite.boot.orm.jdbc.mappers

import info.agilite.core.extensions.concatIfNotNull
import info.agilite.core.extensions.nestedValue

class OneToManyManager(
  private val rootIdName: String,
){
  private val oneToManyList = mutableListOf<OneToMany>()

  fun addOneToMany(oneToMany: OneToMany) {
    oneToManyList.add(oneToMany)
  }

  fun distinct(rows: List<MutableMap<String, Any?>>): List<MutableMap<String, Any?>>{
    val result = mutableListOf<MutableMap<String, Any?>>()
    if(rows.isEmpty()) return result

    var lastRootId: Any? = null
    for(row in rows){
      val rootId = row.nestedValue(rootIdName)
      if(rootId != lastRootId){
        populateAllOneToManyLastId(row)
        result.add(createOneToManyMap(row))
        lastRootId = rootId
      }else{
        for(oneToMany in oneToManyList){
          processOneToManyData( row, oneToMany, result)
        }
      }
    }
    return result
  }

  private fun processOneToManyData(row: MutableMap<String, Any?>, oneToMany: OneToMany, result: MutableList<MutableMap<String, Any?>>) {
    val oneToManyValue = extractOneToManyMap(row, oneToMany) as MutableMap<String, Any?>? ?: return
    val latestOneToManyId = oneToMany.latestOneToManyId
    val oneToManyId = oneToManyValue.nestedValue(oneToMany.collectionColumnId)
    if (latestOneToManyId != oneToManyId) {
      oneToMany.latestOneToManyId = oneToManyId
      val list = extractOrCreateListOfOneToManyMap(result.last(), oneToMany)
      list.add(createOneToManyMap(oneToManyValue), oneToMany.collectionColumnId)
    } else if (oneToMany.children.isNotEmpty()) {
      for (child in oneToMany.children) {
        processOneToManyData(row, child, result)
      }
    }
  }

  private fun populateAllOneToManyLastId(row: MutableMap<String, Any?>){
    for(oneToMany in oneToManyList){
      oneToMany.latestOneToManyId = extractIdOfOneToMany(row, oneToMany)
      populateAllOneToManyLastIdInChildrens(row, oneToMany)
    }
  }

  private fun populateAllOneToManyLastIdInChildrens(row: MutableMap<String, Any?>, parent: OneToMany){
    if(parent.children.isEmpty()) return
    for(oneToMany in parent.children){
      oneToMany.latestOneToManyId = extractIdOfOneToMany(row, oneToMany)
      populateAllOneToManyLastIdInChildrens(row, oneToMany)
    }
  }

  private fun extractIdOfOneToMany(row: MutableMap<String, Any?>, oneToMany: OneToMany): Any?{
    return row.nestedValue(oneToMany.nestedIdName)
  }

  private fun extractOneToManyMap(row: MutableMap<String, Any?>, oneToMany: OneToMany): Any?{
    return row.nestedValue(oneToMany.queryToOneToMany)
  }

  private fun extractOrCreateListOfOneToManyMap(row: MutableMap<String, Any?>, oneToMany: OneToMany): OneToManyList{
    val queryToList = oneToMany.queryToOneToMany.replace("[]", "")

    val keyList = queryToList.split(".")
    var leaf = row[keyList[0]] as OneToManyList?
    for(i in 1 until keyList.size){
      leaf = leaf?.last()?.get(keyList[i]) as OneToManyList?
    }
    return leaf ?: OneToManyList()
  }

  private fun createOneToManyMap(row: MutableMap<String, Any?>): MutableMap<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    for(key in row.keys){
      if(key.endsWith("[]")){
        val value = row[key]

        val listKey = key.substring(0, key.length - 2)

        val oneToManyList = OneToManyList()
        if(value != null){
          oneToManyList.add(createOneToManyMap(value as MutableMap<String, Any?>))
        }
        result[listKey] = oneToManyList
      }else{
        result[key] = row[key]
      }
    }
    return result
  }
}

class OneToMany(
  val nameOfCollection: String,
  val collectionColumnId: String,
){
  val children: MutableList<OneToMany> = mutableListOf()
  private var parentName: String? = null


  val queryToOneToMany: String
    get() {
      return "${parentName.concatIfNotNull(suffix = ".")}$nameOfCollection[]"
    }

  val nestedIdName = "$queryToOneToMany.$collectionColumnId"
  var latestOneToManyId: Any? = null

  fun addOneToMany(child: OneToMany){
    child.parentName = queryToOneToMany
    children.add(child)
  }
}

private class OneToManyList : ArrayList<MutableMap<String, Any?>>(){
  private val ids = mutableSetOf<Any?>()
  private var started = false

  fun add(element: MutableMap<String, Any?>, columnId: String): Boolean {
    if(!started){
      started = true
      ids.addAll(this.map { it[columnId] })
    }
    val elementId = element[columnId]
    if(ids.contains(elementId)) return false
    ids.add(elementId)

    return super.add(element)
  }
}