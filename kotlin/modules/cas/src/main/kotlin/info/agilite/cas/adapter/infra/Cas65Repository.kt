package info.agilite.cas.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cas.Cas65
import org.springframework.stereotype.Repository

@Repository
class Cas65Repository : RootRepository() {
  fun findById(id: Long): Cas65 {
    return findById(Cas65::class, id) ?: throw RuntimeException("Cas65 não encontrado com o id $id")
  }

  fun findOrCreateDefault(): Cas65 {
    findById(Cas65::class, 0L)?.let { return it }

    return Cas65(
      "11111111000111",
      "Padrão",
      "1234"
    ).also {
      it.cas65id = 0L
      insert(it)
    }
  }
}