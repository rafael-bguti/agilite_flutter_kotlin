package info.agilite.shared.utils

import info.agilite.shared.extensions.splitToList
import info.agilite.shared.orm.Entity
import org.springframework.beans.BeanUtils

class ReflectionUtils {
  companion object {
    fun entityIsNew(entity: Any, tableName: String? = null): Boolean {
      return getIdValue(entity, tableName) == null
    }

    fun getIdValue(entity: Any, tableName: String? = null): Long? {
      val localTableName = (tableName ?: entity::class.simpleName!!).lowercase()

      if(entity is Entity){
        return entity.id
      }

      if(entity is Map<*, *>){
        return (entity["${localTableName ?: ""}id"] as Number?)?.toLong()
      }

      return BeanUtils.getPropertyDescriptor(entity::class.java, "${localTableName}id")
        ?.readMethod
        ?.let {
          if(Number::class.java.isAssignableFrom(it.returnType)){
            val number = it.invoke(entity) as Number?
            number?.toLong()
          } else {
            null
          }
        }
    }

    fun setIdValue(entity: Any, id: Long, tableName: String? = null) {
      if(entity is Entity){
        entity.id = id
      }else if(entity is MutableMap<*, *>){
        (entity as MutableMap<String, Any>)["${tableName ?: ""}id"] = id
      }else{
        val localTableName = tableName ?: entity::class.simpleName!!.lowercase()
        BeanUtils.getPropertyDescriptor(entity::class.java, "${localTableName}id")
          ?.writeMethod
          ?.invoke(entity, id)
      }
    }

    fun <T> newInstance(clazz: Class<T>): T {
      return clazz.getDeclaredConstructor().newInstance()
    }

    fun getNestedValue(entity: Any, propertyName: String): Any? {
      val properties = propertyName.splitToList(".")
      var value: Any? = entity
      for (property in properties) {
        if(value == null) return null
        value = getValue(value, property)
      }

      return value
    }

    fun <T> getValue(entity: Any, propertyName: String): T? {
      val readMethod = BeanUtils.getPropertyDescriptor(entity::class.java, propertyName)?.readMethod
        ?: return throw Exception("Property $propertyName not found in ${entity::class.simpleName}")

      return readMethod.invoke(entity) as T?

    }
  }
}