package info.agilite.boot.orm.repositories

import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldTypeMetadata
import info.agilite.boot.metadata.models.OneToManyMetadata
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.core.extensions.splitToList
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.ReflectionUtils.Companion.getValue

internal class EntityInflator(
  private val repo: RootRepository,
) {
  fun inflate(
    entity: AbstractEntity,
    joins: String? = null,
  ){
    if(entity.id == null) throw Exception("Não é possível inflar um objeto sem ID")
    validarJoins(entity, joins)

    val joinsFiltrados = removerJoinsRepetidos(joins)
    val attributesToLoad = extractAttributesToLoad(entity, joinsFiltrados)
    if(attributesToLoad.isEmpty()) return

    val dbQuery = DbQueryBuilders.build(
      entity::class,
      attributesToLoad.joinToString(),
      where = WhereSimple("${entity.javaClass.simpleName}id = :id", mapOf("id" to entity.id))
    )

    repo.distinctMap(dbQuery)?.let {
      JsonUtils.inflateORM(entity, it)
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

  private fun addAllOneToMany(metadata: EntityMetadata, entity: AbstractEntity, attributesToLoad: MutableList<String>, joins: List<String>?) {
    if(metadata.oneToMany.isNullOrEmpty())return

    val oneToManyNotLoaded = metadata.oneToMany.filter {
      precisaBuscarOneToMany(entity, joins, it.key, it.value)
    }

    oneToManyNotLoaded.forEach { (name, oneToMany) ->
      processJoin(joins, name, attributesToLoad, "${oneToMany.childJoinClass.simpleName.lowercase()}id")
    }
  }

  private fun precisaBuscarOneToMany(entity: AbstractEntity, joins: List<String>?, name: String, oneToMany: OneToManyMetadata): Boolean {
    if(!entity.isAttLoaded(name)) return true
    val joinWithOneToManyName = joins?.firstOrNull {
      it.startsWith("$name")
    }
    if(joinWithOneToManyName == null)return false
    if(joinWithOneToManyName.contains("."))throw Exception("Não é possível inflar ForeingKeys de um OneToMany")

    //Validando se as entidades dentro da coleção estão carregadas
    val collection = getValue(entity, name) as Collection<AbstractEntity>? ?: return false
    return collection.any { it.isPartLoaded }
  }

  private fun addAllManyToOne(metadata: EntityMetadata, entity: AbstractEntity, attributesToLoad: MutableList<String>, joins: List<String>?) {
    val fks = metadata.fields.filter { it.type == FieldTypeMetadata.fk }
    if(fks.isEmpty()) return

    val fksNotLoaded = fks.filter {
      !entity.orm.attLoaded.contains(it.attIndex)
        || (getValue(entity, it.name) as AbstractEntity?)?.isPartLoaded == true
    }

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

  private fun processJoin(joins: List<String>?, name: String, attributesToLoad: MutableList<String>, suffix: String? = null) {
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

  private fun addSubJoinsOnLoadedFK(entity: AbstractEntity,  attributesToLoad: MutableList<String>, joinsToThisFk: List<String>, indexToLoadProperty: Int = 0,) {
    joinsToThisFk.forEach {
      val properties = it.splitToList(".")
      var value: AbstractEntity?  = entity
      for ((index, property) in properties.withIndex()) {
        if(index < indexToLoadProperty) continue
        if(value == null) break

        if(!value.isAttLoaded(property)){
          processNestedJoinName(it, attributesToLoad)
          break
        }

//        value = getValue(value!!, property)
//        if(value == null) break
//        if(value.isPartLoaded){
//        }
      }
    }
  }

  private fun addAllSimpleAttNotLoaded(metadata: EntityMetadata, entity: AbstractEntity, attributesToLoad: MutableList<String>) {
    metadata.fields.filter {
      it.type != FieldTypeMetadata.fk && !entity.orm.attLoaded.contains(it.attIndex)
    }.let { notLoaddedSimpleAtts ->
      attributesToLoad.addAll(notLoaddedSimpleAtts.map { it.name })
    }
  }

  private fun removerJoinsRepetidos(join: String?): List<String> {
    if(join == null) return emptyList()

    val joinsOrdenados = join.splitToList().sortedDescending()
    val result = mutableListOf<String>()
    var lastedAdd = ""
    joinsOrdenados.forEach {
      if(!lastedAdd.startsWith(it)) {
        result.add(it)
        lastedAdd = it
      }
    }

    return result
  }

  private fun validarJoins(entity: AbstractEntity, joins: String? = null) {
    if(joins == null) return

    val joinsList = joins.splitToList()
    val metadata = entity.getMetadata()
    val joinsNames = metadata.fields.filter { it.type == FieldTypeMetadata.fk }.map { it.name }
    val oneToManyNames = metadata.oneToMany.keys

    joinsList.forEach {
      if(!joinsNames.contains(it) && !oneToManyNames.contains(it)){
        throw Exception("Join $it não é uma ForeignKey ou OneToMany válida para a entidade ${entity.javaClass.simpleName}")
      }
    }

  }
}