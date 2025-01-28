package info.agilite.boot.sdui.component

import info.agilite.core.extensions.splitToList

const val SHORTCUT_EMPTY = "EMPTY_BOX"

class SduiGrid(
  val rows: List<SduiGridRow>,
  val spacing: Int? = 8,
  val crossAxisAlignment: WrapCrossAlignment? = WrapCrossAlignment.start,
  id: String? = null,
) : SduiComponent(id) {
  companion object {
    fun createByQuery(vararg queries: Any): SduiGrid {
      val result = mutableListOf<SduiGridRow>()

      queries.forEach { q ->
        when (q) {
          is SduiGridRow -> result.add(q)
          is GridRowQuery -> result.add(q.toRow())
          is SduiComponent -> result.add(SduiGridRow("12", listOf(q)))
          is String -> {
            val split = q.splitToList("|")
            val areas = if(split.size == 1) "12" else split[0]
            val metadatas = if(split.size == 1) split[0] else split[1]
            val names = metadatas.splitToList(",")
            val children = names.map { componentFromString(it) }

            result.add(SduiGridRow(areas, children))
          }
          else -> throw RuntimeException("Invalid query type: ${q::class.simpleName}")
        }
      }

      return SduiGrid(result)
    }
  }
}

class GridRowQuery(val areas: String, vararg query: Any){
  private val components = query.toList()

  fun toRow(): SduiGridRow {
    val children = components.map {
      when (it) {
        is SduiComponent -> it
        is String -> componentFromString(it)
        else -> throw RuntimeException("Invalid query type: ${it::class.simpleName}")
      }
    }
    return SduiGridRow(areas, children)
  }
}

data class SduiGridRow(
  val areas: String,
  val children: List<SduiComponent>
)

private fun componentFromString(str: String): SduiComponent {
  return when(str) {
    SHORTCUT_EMPTY -> SduiSizedBox.shrink()
    else -> SduiMetadataField(str)
  }
}