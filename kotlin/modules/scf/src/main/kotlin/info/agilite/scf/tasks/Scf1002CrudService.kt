package info.agilite.scf.tasks

import SduiComboField
import info.agilite.boot.crud.*
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.core.extensions.splitToList
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.scf.SCF02TIPO_PAGAR
import info.agilite.shared.entities.scf.SCF02TIPO_RECEBER
import org.springframework.stereotype.Component

const val STATUS_FILTER = "statusFilter"
@Component
class Scf1002CrudService(crudRepository: AgiliteCrudRepository) : DefaultSduiCrudService<Cgs80>(crudRepository) {
  override fun getColumnQueriesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
    return "scf02dtVenc, scf02lancamento.scf11data, scf02entidade.cgs80nome, scf02entidade.cgs80ni, scf02valor, scf02forma.cgs38nome".splitToList()
  }

  override fun createSduiComponent(request: SduiRequest): SduiComponent {
    val crud = super.createSduiComponent(request) as SduiCrud

    crud.customFilters = listOf(
      SduiSizedBox(
        width = 250.0,
        child = SduiComboField(
          name = STATUS_FILTER,
          options = statusFilterData.map { Option(it.first, it.second) },
          labelText = "Exibir",
          hintText = "Selecione..."
        )
      )
    )

    crud.moreFiltersWidget = SduiSpacingColumn(
      children = listOf(
        SduiDateRange(
          buildMoreFilterKey("scf02dtVenc", MoreFiltersType.BETWEEN, 0),
          buildMoreFilterKey("scf02dtVenc", MoreFiltersType.BETWEEN, 1),
          fluid = true,
          label = "Intervalo de Vencimento"
        ),
      )
    )

    return crud
  }

  override fun convertColumQueryToSduiColumn(columnQuery: List<String>): MutableList<SduiColumn> {
    val result = super.convertColumQueryToSduiColumn(columnQuery)
    result.filter { it.name == "scf02dtVenc" }.first().label = "Dt Vcto"
    result.filter { it.name == "scf02lancamento.scf11data" }.first().label = "Dt Pagto"

    result.add(0, SduiColumn(
      "scf02dtVenc", "Status", "string",
      width = SduiColumnWidth(SduiColumnWidthType.fixed, 100.0),
      mod = MOD_STATUS_DATE_FUNCTION("scf02dtVenc", "scf02lancamento.scf11data"))
    )

    return result
  }

  override fun findListData(taskName: String, request: CrudListRequest): CrudListResponse {
    val response = super.findListData(taskName, request)
    response.selectedGroupIndex = request.groupIndex
    response.groups = listOf(
      CrudListGroup("Receber/Recebidos"),
      CrudListGroup("Pagar/Pagos"),
    )

    return response
  }

  override fun getCustomWhereOnList(
    request: CrudListRequest,
    entityMetadata: EntityMetadata
  ): Pair<String, Map<String, Any>?>? {
    val sb = StringBuilder(" true ")
    if (request.customFilters?.containsKey(STATUS_FILTER) == true) {
      val filterValue = request.customFilters?.get(STATUS_FILTER) as Int
      val where = filterValue?.let {statusFilterData.find { it.first == filterValue }?.third}
      where?.let { sb.append(" AND $it ") }
    }

    if(request.groupIndex == null)request.groupIndex = SCF02TIPO_RECEBER
    sb.append(" AND scf02tipo = ${request.groupIndex}")

    return Pair(sb.toString(), null)
  }

  private val statusFilterData = listOf(
    Triple(null, "Selecionar...", null),
    Triple(0, "Aberto", "scf02dtVenc > now() AND scf02lancamento IS NULL"),
    Triple(1, "Vencido",  "scf02dtVenc < now() AND scf02lancamento IS NULL"),
    Triple(2, "Pago", "scf02lancamento IS NOT NULL"),
  )
}