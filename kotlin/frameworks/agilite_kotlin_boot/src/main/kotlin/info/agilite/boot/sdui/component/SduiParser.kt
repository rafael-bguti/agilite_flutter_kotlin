package info.agilite.boot.sdui.component

import info.agilite.boot.defaultMetadataRepository
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.core.extensions.splitToList
import info.agilite.core.json.JsonUtils
import kotlin.math.max
import kotlin.math.min

const val SDUI_EMPTY = "EMPTY_BOX"

object SduiParser {
  private val componentBuilders = mapOf<String, () -> SduiComponent>(
    SDUI_EMPTY to { SduiSizedBox.shrink() }
  )

  private val columnBuilders = mapOf<String, () -> SduiColumn>(
  )

  fun parseStringToComponents(query: String): List<SduiComponent> {
    return splitQuery(query)
      .map {
        if (componentBuilders.containsKey(it)) {
          componentBuilders[it]!!.invoke()
        } else {
          SduiComponentParsedQuery.fromQuery(it).build()
        }
      }
  }

  fun parseQueryToColumns(vararg query: Any): List<SduiColumn> {
    val listQueries = mutableListOf<SduiColumn>()

    query.forEach { q ->
      when (q) {
        is SduiColumn -> listQueries.add(q)
        is String -> {
          splitQuery(q).forEach {
            if (columnBuilders.containsKey(it)) {
              listQueries.add(columnBuilders[it]!!.invoke())
            } else {
              listQueries.add(SduiColumnFromMetadata.fromQuery(it).build())
            }
          }
        }

        else -> throw RuntimeException("Invalid query type: ${q::class.simpleName} in parseQueryToColumns")
      }
    }

    return listQueries
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
      split.add(sb.toString().trim())
    }

    return split
  }
}

private class SduiComponentParsedQuery(
  val name: String,
  val params: MutableMap<String, String>
) {
  fun build(): SduiComponent {
    return if (name.startsWith("Sdui")) {
      val clazz = Class.forName("info.agilite.boot.sdui.component.$name")
      JsonUtils.fromMap(params, clazz) as SduiComponent
    } else {
      params.put("name", name)
      return JsonUtils.fromMap(params, SduiMetadataField::class.java)
    }
  }

  companion object {
    fun fromQuery(query: String): SduiComponentParsedQuery {
      val (name, params) = parseQuery(query)
      return SduiComponentParsedQuery(name, params)
    }
  }
}

private class SduiColumnFromMetadata(
  val name: String,
  val params: MutableMap<String, String>,
) {
  fun build(): SduiColumn {
    val split = name.split(".")
    val field = defaultMetadataRepository.loadFieldMetadata(split.last())

    val fieldLength = if (field.size <= 5.0 || field.foreignKeyEntity != null) 15 else field.size.toInt()
    val labelLength = field.label.length
    min(max(fieldLength, labelLength), 35)

    val width = SduiColumnWidth(SduiColumnWidthType.byCharCount, min(max(fieldLength, labelLength), 35).toDouble())
    return SduiColumn(
      name = name,
      label = params["label"] ?: getColumnLabel(split),
      type = params["type"] ?: field.type.frontEndType,
      options = field.options?.map { opt -> Option(opt.value, opt.label) },
      width = width,
      mod = params["mod"] ?: field.mod,
    )
  }

  private fun getColumnLabel(columnNames: List<String>): String {
    return if (columnNames.size == 1) {
      defaultMetadataRepository.loadFieldMetadata(columnNames[0]).label
    } else {
      val labels = mutableListOf<String>()
      columnNames.forEach {
        labels.add(defaultMetadataRepository.loadFieldMetadata(it).label)
      }
      labels.joinToString(" - ")
    }
  }

  companion object {
    fun fromQuery(query: String): SduiColumnFromMetadata {
      val (name, params) = parseQuery(query)
      return SduiColumnFromMetadata(name, params)
    }
  }
}

private fun parseQuery(query: String): Pair<String, MutableMap<String, String>> {
  val split = query.splitToList("(")
  val name = split[0]
  val params = if (split.size == 1) {
    mutableMapOf()
  } else {
    split[1].substring(0, split[1].length - 1)
      .splitToList(",")
      .map {
        val paramSplit = it.splitToList(":")
        if (paramSplit.size == 1) throw RuntimeException("Invalid param: $it in query: $query")
        paramSplit[0] to paramSplit[1]
      }
      .toMap()
      .toMutableMap()
  }

  return Pair(name, params)
}
