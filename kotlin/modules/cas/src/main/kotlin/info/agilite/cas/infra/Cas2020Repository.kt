package info.agilite.cas.infra

import info.agilite.cas.domain.Cas2020Model
import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.cache.EntityCache
import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.stereotype.Repository

private val cas2020dtoCache = EntityCache.buildByClass(Cas2020Model::class)
@Repository
class Cas2020Repository : RootRepository() {
  fun findByCas30autenticacao(cas30autenticacao: Long): Cas2020Model? {
    return cas2020dtoCache.getOrPut(cas30autenticacao.toString()) {
      val where = WhereAllEquals(mapOf("cas30autenticacao" to cas30autenticacao))
      uniqueByWhere(Cas2020Model::class, where)
    }
  }

  fun save(cas30: Cas2020Model) {
    save(cas30)
    cas2020dtoCache.put(cas30.cas30autenticacao.toString(), cas30)
  }
}