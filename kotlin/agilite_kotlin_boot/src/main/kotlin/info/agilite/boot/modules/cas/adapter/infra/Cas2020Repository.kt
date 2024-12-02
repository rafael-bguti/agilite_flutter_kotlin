package info.agilite.boot.modules.cas.adapter.infra

import info.agilite.boot.modules.cas.domain.Cas2020Model
import info.agilite.server_core.orm.WhereAllEquals
import info.agilite.server_core.orm.cache.EntityCache
import info.agilite.server_core.orm.repositories.RootRepository
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