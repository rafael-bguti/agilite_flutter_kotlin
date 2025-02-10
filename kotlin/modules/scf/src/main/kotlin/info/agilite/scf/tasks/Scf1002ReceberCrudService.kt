package info.agilite.scf.tasks

import info.agilite.boot.crud.AgiliteCrudRepository
import info.agilite.shared.entities.scf.SCF02TIPO_RECEBER
import org.springframework.stereotype.Component

@Component
class Scf1002ReceberCrudService(
  crudRepository: AgiliteCrudRepository
) : Scf1002CrudService(crudRepository, SCF02TIPO_RECEBER) {
}