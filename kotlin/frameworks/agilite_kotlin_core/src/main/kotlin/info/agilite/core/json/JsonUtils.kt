@file:Suppress("DEPRECATION")

package info.agilite.core.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializer
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import info.agilite.core.orm.AttributeNotLoadedException
import info.agilite.core.orm.Entity
import info.agilite.core.utils.ReflectionUtils
import java.lang.reflect.InvocationTargetException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors


object JsonUtils {
  fun toJson(obj: Any): String {
    return onlyNonNullMapper.writeValueAsString(obj)
  }

  fun toMap(obj: Any): Map<String, Any?> {
    return onlyNonNullMapper.convertValue(obj, object : TypeReference<Map<String, Any?>>() {})
  }

  fun toJsonWithNull(obj: Any): Map<String, Any?> {
    return objectMapper.convertValue(obj, object : TypeReference<Map<String, Any?>>() {})
  }

  fun toMapWithNull(obj: Any): Map<String, Any?> {
    return objectMapper.convertValue(obj, object : TypeReference<Map<String, Any?>>() {})
  }

  fun <T> convertValue(obj: Any, type: TypeReference<T>): T {
    return objectMapper.convertValue(obj, object : TypeReference<T>() {})
  }

  fun inflateORM(entity: Entity, map: Map<String, Any?>){
    val oldAttChanged = entity.getAttChangedIndexes().toSet()
    objectMapper.readerForUpdating(entity).readValue<Any>(objectMapper.writeValueAsString(map))
    clearEntityChanges(entity, oldAttChanged)
  }

  fun <T> fromJson(json: String, clazz: Class<T>): T {
    val value = objectMapper.readValue(json, clazz)
    clearEntityChanges(value)
    return value
  }

  fun <T> fromJson(json: String, typeReference: TypeReference<T>): T {
    val result = objectMapper.readValue(json, typeReference)
    clearEntityChanges(result)

    return result
  }

  fun <T> fromMap(map: Map<String, Any?>, clazz: Class<T>): T {
    val result =  objectMapper.convertValue(map, clazz)
    clearEntityChanges(result)

    return result
  }

  fun toFormatedJson(obj: Any): String {
    return onlyNonNullMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
  }

  fun string2ListMap(json: String): List<Map<String, Any?>> {
    if (json.isBlank()) return emptyList()
    return try {
      objectMapper.readValue(json, object : TypeReference<List<Map<String, Any?>>>() {})
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Erro ao converter string $json", e)
    }
  }

  fun string2Map(json: String): Map<String, Any?> {
    return try {
      objectMapper.readValue(json, object : TypeReference<Map<String, Any?>>() {})
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Erro ao converter string $json", e)
    }
  }

  private fun clearEntityChanges(value: Any?, oldAttChanged: Set<Int>? = null){
    if(value == null) return
    if(value is Entity){
      value.clearChanges(false, oldAttChanged)
    }else if(value is Collection<*>){
      value.forEach { clearEntityChanges(it) }
    }
  }

  private var objectMapper: ObjectMapper = ObjectMapper().also { objectMapper ->
    configureObjectMapper(objectMapper)
    configureLongToEntityObjectMapper(objectMapper)
  }

  private var onlyNonNullMapper: ObjectMapper = ObjectMapper().also { objectMapper ->
    configureObjectMapper(objectMapper)
    configureNotNull(objectMapper)
  }

  private fun configureNotNull(objectMapper: ObjectMapper) {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }

  private fun configureObjectMapper(objectMapper: ObjectMapper) {
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)

    objectMapper.registerModule(createLocalDateModule())
    objectMapper.registerModule(createAttNotLoadedModule())

    objectMapper.registerModule(KotlinModule.Builder().build())
  }

  private fun configureLongToEntityObjectMapper(objectMapper: ObjectMapper){
    val module = SimpleModule()
    module.setDeserializerModifier(OrmDeserializableModifier())
    objectMapper.registerModule(module)
  }

  fun createLocalDateModule(): SimpleModule {
    val module = SimpleModule()
    module.addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ISO_DATE))
    module.addDeserializer(LocalDate::class.java, LocalDateDeserializer(DateTimeFormatter.ISO_DATE))
    module.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    module.addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ISO_TIME))
    module.addDeserializer(LocalTime::class.java, LocalTimeDeserializer(DateTimeFormatter.ISO_TIME))
    return module
  }

  fun createAttNotLoadedModule(): SimpleModule {
    val module = SimpleModule()
    module.setSerializerModifier(PartLoadedEntitySerializerModifier())
    return module
  }
}

internal class OrmDeserializableModifier : BeanDeserializerModifier() {
  override fun modifyDeserializer(config: DeserializationConfig?, beanDesc: BeanDescription?, deserializer: JsonDeserializer<*>?): JsonDeserializer<*> {
    if(Entity::class.java.isAssignableFrom(beanDesc!!.beanClass)){
      return OrmFromLongDeserializer(deserializer as BeanDeserializer)
    }

    return deserializer as JsonDeserializer<*>
  }
}

internal class PartLoadedEntitySerializerModifier : BeanSerializerModifier() {
  override fun changeProperties(config: SerializationConfig?, beanDesc: BeanDescription?, beanProperties: MutableList<BeanPropertyWriter>?): MutableList<BeanPropertyWriter> {
    return beanProperties!!.stream().map { bpw: BeanPropertyWriter? ->
      object : BeanPropertyWriter(bpw) {
        override fun serializeAsField(bean: Any, gen: JsonGenerator, prov: SerializerProvider) {
          try {
            if (bean is Entity) {
              bean.serializing = true
            }

            super.serializeAsField(bean, gen, prov)
          } catch (err: InvocationTargetException) {
            if (err.targetException is AttributeNotLoadedException) {
              //Ignorar atributos não carregados
            } else {
              throw err
            }
          }finally {
            if (bean is Entity) {
              bean.serializing = false
            }
          }
        }
      }
    }.collect(Collectors.toList<BeanPropertyWriter>())
  }
}

class OrmFromLongDeserializer(
  val delegate: BeanDeserializer,
) : BeanDeserializer(delegate as BeanDeserializerBase) {
  override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Any? {
    return if(needConvertLongToEntity(p)) {
      val entity = ReflectionUtils.newInstance(handledType())
      ReflectionUtils.setIdValue(entity, p!!.valueAsLong)
      entity
    }else {
      super.deserialize(p, ctxt)
    }
  }

  private fun needConvertLongToEntity(p: JsonParser?): Boolean {
    return p?.currentToken?.isNumeric == true &&
        Entity::class.java.isAssignableFrom(handledType()) &&
        p.currentToken != JsonToken.VALUE_NULL
  }
}

internal class SkiptWritter(base: BeanPropertyWriter?) : BeanPropertyWriter(base) {
  override fun serializeAsField(bean: Any, gen: JsonGenerator, prov: SerializerProvider) {
    return
  }
}
