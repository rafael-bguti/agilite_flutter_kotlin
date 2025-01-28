package info.agilite.cgs.tasks

import SduiComboField
import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.CrudListRequest
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.*
import info.agilite.shared.entities.cgs.Cgs80
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
          options = listOf(
            Option(null, "Selecionar..."),
            Option("1", "Apenas clientes"),
            Option("2", "Apenas fornecedores"),
            Option("3", "Apenas transportadoras"),
          ),
          labelText = "Exibir",
          hintText = "Selecione..."
        )
      )
    )
    crud.formBody = createFormBody()



    return crud
  }

  //TODO Quadno as opções forem fixas utilizar o combo ao inves do autocomplete
  private fun createFormBody(): SduiComponent {
    return SduiGrid.createByQuery(
      "4,8|cgs80codigo,cgs80nome",
      GridRowQuery(
        "4-4,8-8",
        SduiFieldset(
          "Caracterização",
          SduiSpacingColumn(
            crossAxisAlignment = CrossAxisAlignment.start,
            children = listOf(
              SduiMetadataField("cgs80cliente"),
              SduiMetadataField("cgs80fornecedor"),
              SduiMetadataField("cgs80transportadora"),
            )
          ),
        ),
        SduiPadding(
          top = 8.0,
          child = SduiGrid.createByQuery(
            "6,6|cgs80tipo,cgs80ni,cgs80contribuinte,cgs80ie,cgs80im"
          ),
        )
      ),

      SduiDivider("Contatos"),
      "4,4,4|cgs80email,cgs80telefone,cgs80celular",

      SduiDivider("Endereço"),
      "3-3-5,9-9-7|cgs80cep,cgs80endereco",
      "3-3-4,5-5-4,4|cgs80numero,cgs80bairro,cgs80complemento",
      "3-3-4,5-5-4,4|cgs80uf,cgs80municipio,$SHORTCUT_EMPTY",

      SduiDivider("Observações"),
      "12|cgs80obs"
    )
  }

  override fun getCustomWhereOnList(
    request: CrudListRequest,
    entityMetadata: EntityMetadata
  ): Pair<String, Map<String, Any>?>? {
    if (request.customFilters?.containsKey(CLI_FOR_TRA_FILTER) == true) {
      val filterValue = request.customFilters?.get(CLI_FOR_TRA_FILTER) as String
      val where = when (filterValue) {
        "1" -> "cgs80.cgs80cliente = true"
        "2" -> "cgs80.cgs80fornecedor = true"
        "3" -> "cgs80.cgs80transportadora = true"
        else -> null
      }

      return where?.let { Pair(it, null) }
    }
    return null
  }
}