package info.agilite.shared.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Collectors


class LowerCaseMap : HashMap<String, Any?> {
  constructor(): super()
  constructor(expectedSize: Int): super(expectedSize)

  private constructor(map: Map<*, Any?>): super(map.size){
    if (map != null) {
      for((key, value) in map){
        put(key.toString(), value)
      }
    }
  }
  companion object {
    fun of(map: Map<String, Any?>): LowerCaseMap {
      if(map is LowerCaseMap) return map
      return LowerCaseMap(map)
    }
  }

  override fun put(key: String, value: Any?): Any? {

    return super.put(key.lowercase(), value)
  }

  override fun putAll(from: Map<out String, Any?>) {
    val lowerMap = from.map { it.key.lowercase() to it.value}.toMap()
    super.putAll(lowerMap)
  }

  override fun remove(key: String): Any? {
    return super.remove(key.lowercase())
  }

  override fun remove(key: String, value: Any?): Boolean {
    return super.remove(key.lowercase(), value)
  }

  override fun containsKey(key: String): Boolean {
    return super.containsKey(key.lowercase())
  }

  override fun get(key: String): Any? {
    return super.get(key.lowercase())
  }

  override fun getOrDefault(key: String, defaultValue: Any?): Any? {
    return super.getOrDefault(key.lowercase(), defaultValue)
  }

  override fun putIfAbsent(key: String, value: Any?): Any? {
    return super.putIfAbsent(key.lowercase(), value)
  }

  override fun replace(key: String, oldValue: Any?, newValue: Any?): Boolean {
    return super.replace(key.lowercase(), oldValue, newValue)
  }

  override fun replace(key: String, value: Any?): Any? {
    return super.replace(key.lowercase(), value)
  }

  override fun computeIfAbsent(key: String, mappingFunction: Function<in String, out Any?>): Any? {
    return super.computeIfAbsent(key.lowercase(), mappingFunction)
  }

  override fun computeIfPresent(key: String, remappingFunction: BiFunction<in String, in Any, out Any?>): Any? {
    return super.computeIfPresent(key.lowercase(), remappingFunction)
  }

  override fun compute(key: String, remappingFunction: BiFunction<in String, in Any?, out Any?>): Any? {
    return super.compute(key.lowercase(), remappingFunction)
  }

  override fun merge(key: String, value: Any, remappingFunction: BiFunction<in Any, in Any, out Any?>): Any? {
    return super.merge(key.lowercase(), value, remappingFunction)
  }

  fun getString(key: String): String? {
    return get(key)?.toString() ?: return null
  }

  fun nestMap(): LowerCaseMap {
    val result = LowerCaseMap()
    this.entries.forEach {
      if (!it.key.contains(".")) {
        result[it.key] = it.value
      } else {
        val keyList = it.key.split(".")
        var leaf = result.compute(keyList[0]) { _: String, value: Any? -> value ?: LowerCaseMap() } as LowerCaseMap
        for (i in 1 until keyList.size - 1) {
          leaf = leaf.compute(keyList[i]) { _: String, value: Any? -> value ?: LowerCaseMap() } as LowerCaseMap
        }
        leaf[keyList.last()] = it.value
      }
    }

    return result
  }

  // ---- Getters auxilizares ----
  fun getLong(key: String): Long? {
    val result = get(key) ?: return null
    if (result is Long) return result

    return if(result is String){
      result.toLong()
    } else {
      compute(key) { k, v -> (result as Number).toLong() } as Long
    }
  }

  fun getInteger(key: String): Int? {
    val result = get(key) ?: return null
    if (result is Int) return result
    return if (result is String) {
      result.toInt()
    } else {
      compute(key) { _, v -> (v as Number).toInt() } as Int
    }
  }

  fun getBigDecimal(key: String): BigDecimal? {
    val result = get(key) ?: return null
    return if (result is BigDecimal) {
      result
    } else {
      compute(key) { _, v -> BigDecimal(v.toString()) } as BigDecimal
    }
  }

  fun getLocalDate(key: String): LocalDate? {
    val result = get(key) ?: return null

    return if (result is LocalDate) {
      result
    } else {
      compute(key) { _, v ->
        LocalDate.parse(v.toString())
      } as LocalDate?
    }
  }

  fun getLocalTime(key: String): LocalTime? {
    val result = get(key) ?: return null
    return if (result is LocalTime) {
      result
    } else {
      compute(key) { _, v ->
        LocalTime.parse(v.toString())
      } as LocalTime?
    }
  }

  fun getLocaDateTime(key: String): LocalDateTime? {
    val result = get(key) ?: return null
    return if (result is LocalDateTime) {
      result
    } else {
      compute(key) { _, v ->
        LocalDateTime.parse(v.toString())
      } as LocalDateTime?
    }
    //JSonMapperCreator.create().read("\""+(String)v+"\"", LocalTime.class));
  }

  fun getLowerCaseMap(key: String): LowerCaseMap? {
    val result = get(key) ?: return null
    return if (result is LowerCaseMap) {
      result
    } else {
      compute(key) { _, v -> LowerCaseMap(v as Map<*, Any?>) } as LowerCaseMap
    }
  }

  fun getListLowerCaseMap(key: String): List<LowerCaseMap>? {
    val result = get(key) ?: return null
    if (result !is List<*>) throw RuntimeException("Erro ao obter ListLowerCaseMap, o objeto não é um List para ser convertido")

    if(result.size == 0) return emptyList()
    if(result.get(0) is LowerCaseMap)return result as List<LowerCaseMap>;

    val listLowerCaseMap = result.stream().map { map -> LowerCaseMap((map as Map<*, *>)) }.collect(Collectors.toList())
    put(key, listLowerCaseMap)

    return listLowerCaseMap
  }
}