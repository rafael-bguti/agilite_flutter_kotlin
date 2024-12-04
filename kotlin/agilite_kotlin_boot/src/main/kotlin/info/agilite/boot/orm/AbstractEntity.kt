package info.agilite.boot.orm

import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.core.orm.AttributeNotLoadedException
import info.agilite.core.orm.Entity
import info.agilite.boot.applicationContext
import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.orm.repositories.DefaultRepository
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
    get() = orm.serializing
    set(value) { orm.serializing = value }

  fun validateLoaded(attIndex: Int, name: String) {
    if (orm.isPartLoaded() && !orm.attLoaded.contains(attIndex)) {
      if(orm.serializing){
        throw AttributeNotLoadedException("Attribute $name not loaded")
      }else{
        autoInflate()
      }
    }
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

  override fun clearChanges(executeToCascade: Boolean) {
    ormController.attChanged.clear()

    if(executeToCascade){
      doInCascades { _, entity -> entity.clearChanges() }
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

  private fun doInCascades(process: (index: Int, chid: AbstractEntity) -> Unit ){
    val oneToMany = defaultMetadataRepository.loadEntityMetadata(this.javaClass.simpleName).oneToMany
    for (childKey in oneToMany.keys) {
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

  fun autoInflate(){
    applicationContext.getBean(DefaultRepository::class.java).inflate(this)
  }
}