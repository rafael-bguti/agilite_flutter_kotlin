package info.agilite.boot.sdui.component

class SduiGrid(
  val rows: List<SduiGridRow>,
  val spacing: Int? = 8,
  val crossAxisAlignment: WrapCrossAlignment? = WrapCrossAlignment.start,
  id: String? = null,
) : SduiComponent(id) {
  companion object {
    fun createByRows(vararg queries: GridRowQuery): SduiGrid {
      return SduiGrid(queries.map { it.toRow() })
    }
  }
}

fun row(areas: String, vararg query: Any): GridRowQuery {
  return GridRowQuery(areas, *query)
}
fun row(sduiComponent: SduiComponent): GridRowQuery {
  return GridRowQuery(sduiComponent)
}


class GridRowQuery(private val areas: String, vararg query: Any){
  constructor(sduiComponent: SduiComponent) : this("12", sduiComponent)
  private val components = query.toList()

  fun toRow(): SduiGridRow {
    val children = mutableListOf<SduiComponent>()

    components.forEach {
      when (it) {
        is SduiComponent -> children.add(it)
        is String -> children.addAll(SduiParser.parseStringToComponents(it))
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
