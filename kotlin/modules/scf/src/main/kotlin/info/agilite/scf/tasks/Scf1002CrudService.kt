package info.agilite.scf.tasks

import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.component.MOD_STATUS_DATE_FUNCTION
import info.agilite.boot.sdui.component.SduiColumn
import info.agilite.boot.sdui.component.SduiColumnWidth
import info.agilite.boot.sdui.component.SduiColumnWidthType
import info.agilite.core.extensions.splitToList
import info.agilite.shared.entities.cgs.Cgs80
import org.springframework.stereotype.Component

@Component
class Scf1002CrudService(crudRepository: AgiliteCrudRepository) : DefaultSduiCrudService<Cgs80>(crudRepository) {
  override fun getColumnQueriesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
    return "scf02dtVenc, scf02lancamento.scf11data, scf02tipo, scf02entidade.cgs80nome, scf02entidade.cgs80ni, scf02valor, scf02forma.cgs38nome".splitToList()
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
}