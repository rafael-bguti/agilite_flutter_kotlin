package info.agilite.server_core.orm.repositories

import info.agilite.shared.extensions.splitToList
import info.agilite.shared.json.JsonUtils
import info.agilite.server_core.metadata.models.EntityMetadata
import info.agilite.server_core.metadata.models.FieldTypeMetadata
import info.agilite.server_core.orm.AbstractEntity
import info.agilite.server_core.orm.WhereSimple
import info.agilite.server_core.orm.cache.DefaultEntityCache
import info.agilite.server_core.orm.query.DbQueryBuilders
import info.agilite.shared.utils.ReflectionUtils.Companion.getValue

internal class EntityInflator(
  private val repo: RootRepository,
) {
  fun inflate(
    entity: AbstractEntity,
    joins: String? = null,
  ){
    if(entity.id == null) throw Exception("Não é possível inflar um objeto sem ID")

    val cached = DefaultEntityCache.get(entity.javaClass.simpleName, entity.id.toString()) as AbstractEntity?
    if(cached != null){
      JsonUtils.inflateORM(entity, cached)
    }

    val filteredJoins = filterJoins(joins)
    val attributesToLoad = extractAttributesToLoad(entity, filteredJoins)
    if(attributesToLoad.isEmpty()) return

    val dbQuery = DbQueryBuilders.build(
      entity::class,
      attributesToLoad.joinToString(),
      where = WhereSimple("${entity.javaClass.simpleName}id = :id", mapOf("id" to entity.id))
    )

    repo.distinctMap(dbQuery)?.let {
      JsonUtils.inflateORM(entity, it)
      if(repo.isCacheable(entity::class)){
        repo.entityCache.put(entity.javaClass.name, entity.id.toString(), entity)
      }
    }
  }

  private fun extractAttributesToLoad(entity: AbstractEntity, joins: List<String>?): List<String>{
    val metadata = entity.getMetadata()
    val attributesToLoad = mutableListOf<String>()

    addAllSimpleAttNotLoaded(metadata, entity, attributesToLoad)
    addAllManyToOne(metadata, entity, attributesToLoad, joins)
    addAllOneToMany(metadata, entity, attributesToLoad, joins)

    if(attributesToLoad.isEmpty()) return emptyList()
    attributesToLoad.add(0, entity.javaClass.simpleName.lowercase()+"id")

    return attributesToLoad.distinct()
  }

  private fun addAllOneToMany(
    metadata: EntityMetadata,
    entity: AbstractEntity,
    attributesToLoad: MutableList<String>,
    joins: List<String>?
  ) {
    if(metadata.oneToMany.isNullOrEmpty())return

    val oneToManyNotLoaded = metadata.oneToMany.filter { !entity.orm.attLoaded.contains(it.value.attIndex) }
    oneToManyNotLoaded.forEach { (name, oneToMany) ->
      processJoin(joins, name, attributesToLoad, "${oneToMany.childJoinClass.simpleName.lowercase()}id")
    }

    val oneToManyLoaded = metadata.oneToMany.filter { entity.orm.attLoaded.contains(it.value.attIndex) }
    oneToManyLoaded.forEach { (name, oneToMany) ->
      val joinsToThisFk = joins?.filter { it.startsWith("$name.") }
      if(!joinsToThisFk.isNullOrEmpty()){
        val collection =  getValue(entity, name) as Collection<AbstractEntity>?
        if(collection?.isNotEmpty() == true){
          val firstReg = collection.first()
          addSubJoinsOnLoadedFK(firstReg, attributesToLoad, joinsToThisFk, 1)
        }
      }
    }
  }

  private fun addAllManyToOne(
    metadata: EntityMetadata,
    entity: AbstractEntity,
    attributesToLoad: MutableList<String>,
    joins: List<String>?
  ) {
    val fks = metadata.fields.filter { it.type == FieldTypeMetadata.fk }
    if(fks.isEmpty()) return

    val fksNotLoaded = fks.filter { !entity.orm.attLoaded.contains(it.attIndex) }
    fksNotLoaded.forEach {
      val name = it.name
      processJoin(joins, name, attributesToLoad)
    }

    val fksLoaded = fks.filter { entity.orm.attLoaded.contains(it.attIndex) }
    fksLoaded.forEach { fkLoaded ->
      val name = fkLoaded.name
      val joinsToThisFk = joins?.filter { it.startsWith("$name.") }
      if(!joinsToThisFk.isNullOrEmpty()){
        addSubJoinsOnLoadedFK(entity, attributesToLoad, joinsToThisFk)
      }
    }
  }

  private fun processJoin(
    joins: List<String>?,
    name: String,
    attributesToLoad: MutableList<String>,
    suffix: String? = null
  ) {
    var addedJoin = false
    if (joins?.isNotEmpty() == true) {
      for (joinName in joins) {
        if (joinName.equals(name, true) || joinName.startsWith("$name.")) {
          addedJoin = true
          processNestedJoinName(joinName, attributesToLoad)
        }
      }
    }

    if (!addedJoin) {
      attributesToLoad.add("$name${suffix?.let { ".$it" } ?: ""}")
    }
  }

  private fun processNestedJoinName(join: String, attributesToLoad: MutableList<String>,){
    val nestedJoin = join.split(".")

    var joinNameHistory = ""
    for(joinName in nestedJoin){
      joinNameHistory += "$joinName."
      attributesToLoad.add("$joinNameHistory*")
    }
  }

  private fun addSubJoinsOnLoadedFK(
    entity: AbstractEntity,
    attributesToLoad: MutableList<String>,
    joinsToThisFk: List<String>,
    indexToLoadProperty: Int = 0,
  ) {
    joinsToThisFk.forEach {
      val properties = it.splitToList(".")
      var value: AbstractEntity?  = entity
      for ((index, property) in properties.withIndex()) {
        if(index < indexToLoadProperty) continue

        value = getValue(value!!, property)
        if(value == null) break
        if(value.isPartLoaded){
          processNestedJoinName(it, attributesToLoad)
          break
        }
      }
    }
  }

  private fun addAllSimpleAttNotLoaded(
    metadata: EntityMetadata,
    entity: AbstractEntity,
    attributesToLoad: MutableList<String>
  ) {
    metadata.fields.filter { it.type != FieldTypeMetadata.fk && !entity.orm.attLoaded.contains(it.attIndex) }
      .let { notLoaddedSimpleAtts ->
        notLoaddedSimpleAtts.forEach { attributesToLoad.add(it.name) }
      }
  }

  private fun filterJoins(join: String?): List<String> {
    if(join == null) return emptyList()

    val orderList = join.splitToList().sortedDescending()
    val filteredList = mutableListOf<String>()
    var lastedAdd = ""
    orderList.forEach {
      if(!lastedAdd.startsWith(it)) {
        filteredList.add(it)
        lastedAdd = it
      }
    }

    return filteredList
  }
}