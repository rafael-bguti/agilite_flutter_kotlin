package info.agilite.scf.tasks

import SduiComboField
import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.CrudListRequest
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.crud.MoreFiltersType
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.tasks.TaskDescr
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.core.extensions.splitToList
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.scf.SCF02TIPO_RECEBER
import java.time.LocalDate

const val STATUS_FILTER = "statusFilter"

class Scf1002CrudService(
  crudRepository: AgiliteCrudRepository,
  private val scf02tipo: Int,
) : DefaultSduiCrudService<Cgs80>(crudRepository) {
  override val createSduiFormOnEdit : Boolean get() = true

  override fun getColumnQueriesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
    return "scf02dtVenc, scf02valor, scf02nossoNum, scf02lancamento.scf11data, scf02entidade.cgs80nome, scf02entidade.cgs80ni, scf02forma.cgs38nome".splitToList()
  }

  override fun createSduiComponent(request: SduiRequest): SduiComponent {
    val crud = super.createSduiComponent(request) as SduiCrud

    crud.descr =
      if(scf02tipo == SCF02TIPO_RECEBER) {
        TaskDescr("Contas a Receber", "Contas a Receber")
      } else {
        TaskDescr("Contas a Pagar", "Contas a Pagar")
      }

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
      "status", "Status", "string", width = SduiColumnWidth(SduiColumnWidthType.fixed, 100.0), mod = MOD_BADGE)
    )

    return result
  }

  override fun parseData(data: List<MutableMap<String, Any?>>): List<MutableMap<String, Any?>> {
    data.forEach {
      val vcto = it["scf02dtVenc"] as LocalDate
      val pgto = it["scf02lancamento.scf11data"] as LocalDate?

      if (pgto != null) {
        it["status"] = "Pago|${0xFF00AA00}"
      } else {
        val hoje = LocalDate.now()
        if (vcto.isBefore(hoje)) {
          it["status"] = "Vencido|${0xFFAA0000}"
        } else {
          it["status"] = "A vencer|${0xFFBDBDBD}"
        }
      }
    }

    return data
  }

  override fun getCustomWhereOnList(request: CrudListRequest, entityMetadata: EntityMetadata): Pair<String, Map<String, Any>?>? {
    val sb = StringBuilder(" true ")
    if (request.customFilters?.containsKey(STATUS_FILTER) == true) {
      val filterValue = request.customFilters?.get(STATUS_FILTER) as Int
      val where = filterValue.let {statusFilterData.find { it.first == filterValue }?.third}
      where?.let { sb.append(" AND $it ") }
    }

    sb.append(" AND scf02tipo = ${scf02tipo}")

    return Pair(sb.toString(), null)
  }

  override fun createSduiEditForm(taskName: String, id: Long?): SduiComponent? {
    return SduiGrid.createByRows(
      row("6,6", "scf02forma, scf02entidade"),
      row("4,4,4",  SduiMetadataField("scf02nossoNum", "Número"), "scf02dtVenc, scf02valor"),
      row("3-6,3-6,3-6,3-6",  "scf02multa, scf02juros, scf02encargos, scf02desconto"),
      row("12", "scf02hist"),
    )
  }

  private val statusFilterData = listOf(
    Triple(null, "Selecionar...", null),
    Triple(0, "Aberto", "scf02dtVenc > now() AND scf02lancamento IS NULL"),
    Triple(1, "Vencido",  "scf02dtVenc < now() AND scf02lancamento IS NULL"),
    Triple(2, "Pago", "scf02lancamento IS NOT NULL"),
  )
}
