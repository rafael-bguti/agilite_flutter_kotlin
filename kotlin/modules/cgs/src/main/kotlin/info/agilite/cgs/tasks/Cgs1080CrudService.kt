package info.agilite.cgs.tasks

import SduiComboField
import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.CrudListRequest
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.SduiRequest
import info.agilite.boot.sdui.autocomplete.Option
import info.agilite.boot.sdui.component.SduiComponent
import info.agilite.boot.sdui.component.SduiCrud
import info.agilite.boot.sdui.component.SduiSizedBox
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

    return crud
  }

  override fun getCustomWhereOnList(request: CrudListRequest, entityMetadata: EntityMetadata): Pair<String, Map<String, Any>?>? {
    if(request.customFilters?.containsKey(CLI_FOR_TRA_FILTER) == true) {
      val filterValue = request.customFilters?.get(CLI_FOR_TRA_FILTER) as String
      val where = when(filterValue) {
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