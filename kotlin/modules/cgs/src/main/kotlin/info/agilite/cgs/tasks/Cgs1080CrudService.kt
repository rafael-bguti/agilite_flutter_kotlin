package info.agilite.cgs.tasks

import SduiComboField
import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.CrudListRequest
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.core.exceptions.ValidationException
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.cgs.N_CGS80_CLIENTE
import org.springframework.stereotype.Component

private const val CLI_FOR_TRA_FILTER = "cliForTraFilter"

@Component
class Cgs1080CrudService(
  crudRepository: AgiliteCrudRepository
) : DefaultSduiCrudService<Cgs80>(crudRepository) {
  override fun createSduiComponent(request: SduiRequest): SduiComponent {
    val crud = super.createSduiComponent(request) as SduiCrud
    crud.customFilters = listOf(
      SduiSizedBox(
        width = 250.0,
        child = SduiComboField(
          name = CLI_FOR_TRA_FILTER,
          options = entidadeFilterData.map { Option(it.first, it.second) },
          labelText = "Exibir",
          hintText = "Selecione..."
        )
      )
    )
    return crud
  }

  override fun createSduiEditForm(taskName: String, id: Long?): SduiComponent {
    return createFormBody()
  }

  override fun getCustomWhereOnList(
    request: CrudListRequest,
    entityMetadata: EntityMetadata
  ): Pair<String, Map<String, Any>?>? {
    if (request.customFilters?.containsKey(CLI_FOR_TRA_FILTER) == true) {
      val filterValue = request.customFilters?.get(CLI_FOR_TRA_FILTER) as String
      val where = filterValue?.let {entidadeFilterData.find { it.first == filterValue }?.third}
      return where?.let { Pair(it, null) }
    }
    return null
  }

  override fun buildMapOnCreateNewRecord(task: String): Map<String, Any?> {
    return mapOf(
      N_CGS80_CLIENTE to true,
    )
  }

  override fun validate(entity: Cgs80) {
    if (entity.cgs80cliente == false && entity.cgs80fornecedor == false && entity.cgs80transportadora == false) {
      throw ValidationException("O cadastro precisa ser caracterizado: Cliente, Fornecedor ou Transportadora")
    }
  }

  private fun createFormBody(): SduiComponent {
    return SduiGrid.createByRows(
      row("4,8", "cgs80codigo,cgs80nome"),
      row("4-4,8-8", caracterizacaoFieldset, niFields),
      row(SduiDivider("Contatos")),
      row("4,4,4", "cgs80email,cgs80telefone,cgs80celular"),
      row(SduiDivider("Endereço")),
      row("3-3-5,9-9-7", "cgs80cep,cgs80endereco"),
      row("3-3-4,5-5-4,4", "cgs80numero,cgs80bairro,cgs80complemento"),
      row("3-3-4,5-5-4,4", "cgs80uf,cgs80municipio,$SDUI_EMPTY"),
      row(SduiDivider("Observações")),
      row("12", "cgs80obs"),
    )
  }
  private val caracterizacaoFieldset: SduiFieldset
    get() {
      return SduiFieldset(
        "Caracterização",
        SduiSpacingColumn(
          crossAxisAlignment = CrossAxisAlignment.start,
          children = listOf(
            SduiMetadataField("cgs80cliente"),
            SduiMetadataField("cgs80fornecedor"),
            SduiMetadataField("cgs80transportadora"),
          )
        ),
      )
    }

  private val niFields: SduiComponent
    get() {
      return SduiPadding(
        top = 8.0,
        child = SduiGrid.createByRows(
          GridRowQuery(
            "6,6", "cgs80tipo,cgs80ni,cgs80contribuinte,cgs80ie,cgs80im",
          )
        ),
      )
    }

  private val entidadeFilterData = listOf(
    Triple(null, "Selecionar...", null),
    Triple("1", "Apenas clientes", "cgs80.cgs80cliente = true"),
    Triple("2", "Apenas fornecedores",  "cgs80.cgs80fornecedor = true"),
    Triple("3", "Apenas transportadoras", "cgs80.cgs80transportadora = true"),
  )
}