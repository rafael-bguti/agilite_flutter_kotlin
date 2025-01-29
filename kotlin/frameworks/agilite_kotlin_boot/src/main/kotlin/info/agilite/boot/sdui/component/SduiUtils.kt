package info.agilite.boot.sdui.component

import info.agilite.core.extensions.splitToList
import info.agilite.core.json.JsonUtils

const val SDUI_EMPTY = "EMPTY_BOX"

object SduiUtils {
  private val componentBuilders = mapOf<String, () -> SduiComponent>(
    SDUI_EMPTY to { SduiSizedBox.shrink() }
  )

  fun parseStringToComponents(query: String): List<SduiComponent> {
    return splitQuery(query)
      .map { it.trim() }
      .map {
        if (componentBuilders.containsKey(it)) {
          componentBuilders[it]!!.invoke()
        } else {
          SduiParsedQuery.fromQuery(it).build()
        }
      }
  }

  private fun splitQuery(query: String): List<String> {
    val split = mutableListOf<String>();
    val sb = StringBuilder()
    var inParams = false

    for (i in 0..<query.length) {
      val char = query[i]
      if (char == '(') {
        inParams = true
      } else if (char == ')') {
        inParams = false
      }

      if (char == ',' && !inParams) {
        split.add(sb.toString())
        sb.clear()
      } else {
        sb.append(char)
      }
    }
    if (sb.isNotEmpty()) {
      split.add(sb.toString())
    }

    return split
  }
}

private class SduiParsedQuery(
  val name: String,
  val params: MutableMap<String, String>
) {
  fun build(): SduiComponent {
    return if(name.startsWith("Sdui")){
      val clazz = Class.forName("info.agilite.boot.sdui.component.$name")
      JsonUtils.fromMap(params, clazz) as SduiComponent
    } else {
      params.put("name", name)
      return JsonUtils.fromMap(params, SduiMetadataField::class.java)
    }

  }

  companion object {
    fun fromQuery(query: String): SduiParsedQuery {
      val split = query.splitToList("(")
      val name = split[0]
      val params = if (split.size == 1) {
        mutableMapOf()
      } else {
        split[1].substring(0, split[1].length - 1)
          .splitToList(",")
          .map {
            val paramSplit = it.splitToList(":")
            if(paramSplit.size == 1) throw RuntimeException("Invalid param: $it in query: $query")
            paramSplit[0] to paramSplit[1]
          }
          .toMap()
          .toMutableMap()
      }

      return SduiParsedQuery(name, params)
    }
  }
}
