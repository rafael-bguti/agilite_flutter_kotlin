package info.agilite.scf.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.scf.*
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
class Scf021Repository: RootRepository() {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun insertInNewTransaction(scf021: Scf021) {
    insert(scf021)
  }

}