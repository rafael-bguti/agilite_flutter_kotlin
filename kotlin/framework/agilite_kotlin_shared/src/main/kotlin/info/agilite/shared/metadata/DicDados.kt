package info.agilite.shared.metadata

import info.agilite.boot.metadata.data.MetadataDatasource
import info.agilite.boot.metadata.models.AutocompleteConfig
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.tasks.TaskMetadata
import info.agilite.boot.orm.AbstractEntity
import info.agilite.core.extensions.localCapitalize
import info.agilite.core.extensions.substr
import info.agilite.core.utils.ReflectionUtils
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

private val entitiesMetadataCache = mutableMapOf<String, EntityMetadata>()

class DicDados : MetadataDatasource {
  val customAutocompleteConfig = mapOf<String, AutocompleteConfig>()

  override fun getEntityClass(entityName: String): KClass<*>? {
    val moduleName = entityName.substr(0, 3)
    return Class.forName("info.agilite.${moduleName.lowercase()}.domain.entities.${entityName.lowercase().localCapitalize()}").kotlin
  }

  override fun getEntity(entityName: String): EntityMetadata? {
    return entitiesMetadataCache.getOrPut(entityName.uppercase()) {
      val entityClass = getEntityClass(entityName) ?: return null

      val instance = ReflectionUtils.newInstance(entityClass.java) as AbstractEntity
      instance.getMetadata()
    }
  }

  override fun getTask(taskCode: String): TaskMetadata? {
    //TODO Implementar
    return null
  }

  override fun getAutocompleteConfigByFieldName(fieldName: String): AutocompleteConfig? {
    return customAutocompleteConfig[fieldName.lowercase()]
  }
}