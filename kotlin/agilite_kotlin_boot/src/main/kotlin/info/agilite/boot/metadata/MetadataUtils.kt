package info.agilite.boot.metadata

import info.agilite.core.json.JsonUtils
import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.FieldTypeMetadata
import org.postgresql.util.PGobject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MetadataUtils {
  companion object {
    fun extractEntityNameFromFieldName(fieldName: String): String {
      val sb = StringBuilder()
      var numberStarted = false
      for (i in fieldName.indices) {
        val c = fieldName[i]
        if (c.isDigit()){
          numberStarted = true
          sb.append(c)
        }else{
          if(numberStarted) break
          sb.append(c)
        }
      }
      return sb.toString()
    }

    fun convertValueByFieldName(fieldName: String, value: Any?): Any? {
      return defaultMetadataRepository.loadFieldMetadata(fieldName).let { convertValueByField(it, value) }
    }

    fun convertValueByField(field: FieldMetadata, value: Any?): Any? {
      if(value == null) return null
      val type = field.type

      return when (type) {
        FieldTypeMetadata.id,
        FieldTypeMetadata.long,
        FieldTypeMetadata.fk -> convertId(field, value)
        FieldTypeMetadata.string,
        FieldTypeMetadata.text -> value.toString()
        FieldTypeMetadata.int -> if(value is Int) value else value.toString().toInt()
        FieldTypeMetadata.decimal,
        FieldTypeMetadata.money -> if(value is Double) value else value.toString().toDouble()
        FieldTypeMetadata.date -> if(value is LocalDate) value else LocalDate.parse(value.toString())
        FieldTypeMetadata.dateTime -> if(value is LocalDateTime) value else LocalDateTime.parse(value.toString())
        FieldTypeMetadata.time -> if(value is LocalTime) value else LocalTime.parse(value.toString())
        FieldTypeMetadata.boolean -> if(value is Boolean) value else value.toString().toBoolean()
        FieldTypeMetadata.json -> parseJsonToPgObject(value)
      }
    }

    private fun parseJsonToPgObject(value: Any): PGobject {
      val json = JsonUtils.toJson(value)
      val jsonObject = PGobject()
      jsonObject.type = "json"
      jsonObject.value = json
      return jsonObject
    }

    private fun convertId(field: FieldMetadata, value: Any): Long? {
      return if(value is Number) {
        value.toLong()
      }else if(value is Map<*, *>) {
        if(field.foreignKeyEntity == null) {
          if(value["id"] is Number) {
            (value["id"] as Number).toLong()
          }
          return null
        }else{
          val idName = "${field.foreignKeyEntity.lowercase()}id"
          (value[idName] as Number?)?.toLong()
        }
      }else{
        value?.toString()?.toLongOrNull()
      }
    }
  }
}