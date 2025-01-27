package info.agilite.shared.metadata

import info.agilite.boot.metadata.data.MetadataDatasource
import info.agilite.boot.metadata.models.AutocompleteConfig
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.where
import info.agilite.core.extensions.localCapitalize
import info.agilite.core.extensions.substr
import info.agilite.core.model.LowerCaseMap
import info.agilite.core.utils.ReflectionUtils
import info.agilite.shared.entities.cgs.*
import kotlin.reflect.KClass

private val entitiesMetadataCache = mutableMapOf<String, EntityMetadata>()

class DicDados : MetadataDatasource {
  private val customAutocompleteConfig = LowerCaseMap.of(mapOf<String, AutocompleteConfig>(
     "cgs38RecBoletoComApi" to AutocompleteConfig(
       field = CGS38NOME,
       columnsToSelect = "cgs38nome",
       table = "Cgs38",
       defaultWhere = where {
         and {
           eq(
             N_CGS38_TIPO, CGS38TIPO_RECEBIMENTO,
             N_CGS38_FORMA, CGS38FORMA_BOLETO,
           )
           isNotNull(N_CGS38_API_CLIENT_ID)
         }
       },
     )
  ))

  override fun getEntityClass(entityName: String): KClass<*>? {
    val moduleName = entityName.substr(0, 3)
    return try {
      Class.forName("info.agilite.shared.entities.${moduleName.lowercase()}.${entityName.lowercase().localCapitalize()}").kotlin
    } catch (e: ClassNotFoundException) {
      null
    }

  }

  override fun getEntity(entityName: String): EntityMetadata? {
    return entitiesMetadataCache.getOrPut(entityName.uppercase()) {
      val entityClass = getEntityClass(entityName) ?: return null

      val instance = ReflectionUtils.newInstance(entityClass.java) as AbstractEntity
      instance.getMetadata()
    }
  }


  override fun getAutocompleteConfigByFieldName(fieldName: String): AutocompleteConfig? {
    return customAutocompleteConfig[fieldName] as AutocompleteConfig?
  }
}