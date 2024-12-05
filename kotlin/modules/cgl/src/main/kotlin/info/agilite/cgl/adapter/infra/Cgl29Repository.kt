package info.agilite.cgl.adapter.infra

import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.cache.GlobalEntityCache
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.cgl.domain.entities.Cgl29
import info.agilite.cgl.domain.entities.Cgl30
import org.springframework.stereotype.Repository

@Repository
class Cgl29Repository : RootRepository() {
  fun findOrCreateDefault(): Cgl29 {
    findById(Cgl29::class, 0L)?.let { return it }

    return Cgl29("Padrão").also {
      it.cgl29id = 0L;
      insert(it)
    }
  }
}