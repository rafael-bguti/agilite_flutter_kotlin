package info.agilite.cas.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cas.Cas29
import org.springframework.stereotype.Repository

@Repository
class Cas29Repository : RootRepository() {
  fun findOrCreateDefault(): Cas29 {
    findById(Cas29::class, 0L)?.let { return it }

    return Cas29("Padrão").also {
      it.cas29id = 0L
      insert(it)
    }
  }
}