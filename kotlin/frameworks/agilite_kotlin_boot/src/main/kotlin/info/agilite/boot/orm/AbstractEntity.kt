package info.agilite.boot.orm

import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.core.orm.AttributeNotLoadedException
import info.agilite.core.orm.Entity
import info.agilite.boot.applicationContext
import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.orm.repositories.DefaultRepository
import info.agilite.core.json.JsonUtils
import info.agilite.core.model.LowerCaseMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.full.memberProperties

const val UID_TO_ORM_KEY = "uidToOrm"
abstract class AbstractEntity(
  private val propertiesCount: Int? = null
) : Entity {
  @JsonIgnore
  abstract fun getMetadata(): EntityMetadata

  var uidToOrm: Long? = null

  @JsonIgnore
  private val ormController = ORMController(propertiesCount ?: 0)


  val isNew: Boolean
    @JsonIgnore get() = id == null


  override var serializing: Boolean
    @JsonIgnore get() = orm.serializing
    set(value) { orm.serializing = value }

  fun validateLoaded(attIndex: Int, name: String, required: Boolean = false) {
    if(!orm.attLoaded.contains(attIndex)){
      if (orm.isPartLoaded()) {
        if(orm.serializing){
          throw AttributeNotLoadedException("Attribute $name not loaded")
        }else{
          autoInflate()
        }
      }else if(required){
        throw AttributeNotLoadedException("Attribute $name not loaded")
      }
    }
  }

  protected fun isIdDefined(): Boolean {
    return orm.attLoaded.contains(0)
  }

  val orm: ORMController
    @JsonIgnore get() = ormController

  val attChanged: Set<Int>
    @JsonIgnore get() = ormController.attChanged

  val isChanged: Boolean
    @JsonIgnore get() = attChanged.isNotEmpty()

  val isPartLoaded: Boolean
    @JsonIgnore get() = ormController.isPartLoaded()

  internal fun createUidsToCascadeOrm(uuidGen: AtomicLong) {
    uidToOrm = uuidGen.incrementAndGet()

    doInCascades { _, entity -> entity.createUidsToCascadeOrm(uuidGen) }
  }

  fun extractMapOfChagedProperties(includeOneToMany: Boolean = true): LowerCaseMap {
    val chagedPropertiesName = loadChangedPropertieNames(includeOneToMany)

    return LowerCaseMap.of(JsonUtils.toMapWithNull(this).filterKeys {
      it.lowercase() == "${javaClass.simpleName}id".lowercase() || chagedPropertiesName.contains(it.lowercase())
    })
  }

  override fun clearChanges(executeToCascade: Boolean) {
    ormController.attChanged.clear()

    if(executeToCascade){
      doInCascades (false) { _, entity -> entity.clearChanges() }
    }
  }

  internal fun dbSaved(executeToCascade: Boolean = true) {
    ormController.setDbSaved()
    if(executeToCascade){
      doInCascades { _, entity -> entity.dbSaved() }
    }
  }

  internal fun setIdsByUidToCascadeOrm(mapOfUidToCascade: MutableMap<Long, Long>) {
    val id = this.id
    if(id == null){
      val uid = this.uidToOrm ?: throw RuntimeException("uidToOrm is null on Entity to set inserted ID")
      val insertedId = mapOfUidToCascade[uid] ?: throw RuntimeException("Inserted ID not found for uidToOrm $uid")
      this.id = insertedId
    }

    doInCascades { _, entity -> entity.setIdsByUidToCascadeOrm(mapOfUidToCascade) }
  }

  private fun doInCascades(autoInflate: Boolean = true, process: (index: Int, chid: AbstractEntity) -> Unit ){
    val oneToMany = defaultMetadataRepository.loadEntityMetadata(this.javaClass.simpleName).oneToMany
    for (childKey in oneToMany.keys) {
      val oneToMany = oneToMany[childKey]!!
      if(orm.isPartLoaded()){
        if(!orm.isAttLoaded(oneToMany.attIndex) && !autoInflate){
          return
        }
      }

      val property = this::class.memberProperties.find { it.name == childKey }
      val childList = property!!.getter.call(this)

      if (childList is Collection<*>) {
        childList.forEachIndexed{ index, element ->
          if (element is AbstractEntity) {
            process(index, element)
          }
        }
      }
    }
  }

  internal fun isAttLoaded(attName: String): Boolean {
    getMetadata().fields.forEach { field ->
      if(field.name.lowercase() == attName.lowercase()){
        return orm.isAttLoaded(field.attIndex)
      }
    }
    getMetadata().oneToMany.forEach { (name, oneToMany) ->
      if(name.lowercase() == attName.lowercase()){
        return orm.isAttLoaded(oneToMany.attIndex)
      }
    }
    throw RuntimeException("Attribute $attName not found in ${this.javaClass.simpleName}")
  }

  fun autoInflate(){
    applicationContext.getBean(DefaultRepository::class.java).inflate(this)
  }

  private fun loadChangedPropertieNames(includeOneToMany: Boolean): Set<String> {
    val metadata = this.getMetadata()
    val setOfChangedProperties = this.attChanged
    val result = mutableSetOf<String>()
    metadata.fields.forEach { field ->
      if (setOfChangedProperties.contains(field.attIndex)) {
        result.add(field.name.lowercase())
      }
    }

    if(includeOneToMany) {
      metadata.oneToMany.entries.forEach { oneToMany ->
        if (setOfChangedProperties.contains(oneToMany.value.attIndex)) {
          result.add(oneToMany.key.lowercase())
        }
      }
    }

    return result
  }
}