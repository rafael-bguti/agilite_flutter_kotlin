package info.agilite.scf.tasks

import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.boot.crud.DefaultSduiCrudService
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.sdui.component.SduiParser
import info.agilite.core.extensions.splitToList
import info.agilite.shared.entities.cgs.Cgs80
import org.springframework.stereotype.Component

@Component
class Scf1002CrudService(crudRepository: AgiliteCrudRepository) : DefaultSduiCrudService<Cgs80>(crudRepository) {
  override fun getColumnQueriesToList(taskName: String, entityMetadata: EntityMetadata): List<String> {
    return "scf02dtVenc, scf02lancamento.scf11data, scf02tipo, scf02entidade.cgs80nome, scf02entidade.cgs80ni, scf02valor, scf02forma.cgs38nome".splitToList()
  }
}