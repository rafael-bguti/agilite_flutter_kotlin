package info.agilite.scf.tasks

import SduiComboField
import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.CrudListRequest
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.crud.MoreFiltersType
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.tasks.TaskDescr
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.core.extensions.*
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.scf.N_SCF02_DT_EMISS
import info.agilite.shared.entities.scf.SCF02LANCAMENTO
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

  override fun processListData(data: List<MutableMap<String, Any?>>): List<MutableMap<String, Any?>> {
    data.forEach {
      val vcto = it.getLocalDate("scf02dtVenc")!!
      val pgto = it.getLocalDate("scf02lancamento.scf11data")
      val statusPair = statusPagamentoTagText(pgto, vcto)
      it["status"] = "${statusPair.first}|${statusPair.second}"
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

  override fun getColumNamesToJoin(taskName: String,  parentAlias: String?, parenteFkField: FieldMetadata, onEdit: Boolean): List<String> {
    if(onEdit && parenteFkField == SCF02LANCAMENTO){
      return listOf("scf02lancamento.*")
    }else{
      return super.getColumNamesToJoin(taskName, parentAlias, parenteFkField, onEdit)
    }
  }

  override fun createSduiEditForm(taskName: String, id: Long?, data: Map<String, Any?>): SduiComponent? {
    val rows = mutableListOf(
      row("6,6", "scf02forma, scf02entidade"),
      row("4,4,4",  SduiMetadataField("scf02nossoNum", "Número"), "scf02dtVenc, scf02valor"),
      row("3-6,3-6,3-6,3-6",  "scf02multa, scf02juros, scf02encargos, scf02desconto"),
      row("12", "scf02hist"),
    )
    if(id != null){

      val dtPagto = data.getMap("scf02lancamento")?.getLocalDate("scf11data")
      rows.add(0,
        row("3-6, 3-6",
        SduiMetadataField(N_SCF02_DT_EMISS, enabled = false),
        SduiTextField("scf11data", FieldType.date,  labelText = "Data de Pagto", enabled = false, initialValue = dtPagto?.format()),
      ))
    }

    return SduiGrid.createByRows(*rows.toTypedArray())
  }

  private val statusFilterData = listOf(
    Triple(null, "Selecionar...", null),
    Triple(0, "Aberto", "scf02dtVenc > now() AND scf02lancamento IS NULL"),
    Triple(1, "Vencido",  "scf02dtVenc < now() AND scf02lancamento IS NULL"),
    Triple(2, "Pago", "scf02lancamento IS NOT NULL"),
  )

  private fun statusPagamentoTagText(pgto: LocalDate?, vcto: LocalDate) : Pair<String, Long> {
    if (pgto != null) {
      return Pair("Pago", 0xFF00AA00)
    } else {
      val hoje = LocalDate.now()
      if (vcto.isBefore(hoje)) {
        return Pair("Vencido", 0xFFAA0000)
      } else {
        return Pair("A vencer", 0xFFBDBDBD)
      }
    }
  }
}
