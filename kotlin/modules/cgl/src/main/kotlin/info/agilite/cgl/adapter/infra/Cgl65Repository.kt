package info.agilite.cgl.adapter.infra

import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.cache.GlobalEntityCache
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.cgl.domain.entities.Cgl29
import info.agilite.cgl.domain.entities.Cgl30
import info.agilite.cgl.domain.entities.Cgl65
import org.springframework.stereotype.Repository

@Repository
class Cgl65Repository : RootRepository() {
  fun findOrCreateDefault(): Cgl65 {
    findById(Cgl65::class, 0L)?.let { return it }

    return Cgl65(
      "11111111000111",
      "Padrão",
    ).also {
      it.cgl65id = 0L
      insert(it)
    }
  }
}