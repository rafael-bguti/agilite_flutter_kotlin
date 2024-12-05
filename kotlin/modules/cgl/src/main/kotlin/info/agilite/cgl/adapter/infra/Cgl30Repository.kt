package info.agilite.cgl.adapter.infra

import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.cache.EntityCache
import info.agilite.boot.orm.cache.GlobalEntityCache
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.cgl.domain.entities.Cgl30
import info.agilite.cgl.domain.entities.N_CGL30AUTENTICACAO
import org.springframework.stereotype.Repository

private val autenticacaoCache = EntityCache.buildByClass(Cgl30::class)
@Repository
class Cgl30Repository : RootRepository() {
  init {
    GlobalEntityCache.addRemovalListener {
      if(it.value is Cgl30) {
        autenticacaoCache.invalidate((it.value as Cgl30).cgl30autenticacao.toString())//TODO: testar
      }
    }
  }

  fun findByCgl30autenticacao(cgl30autenticacao: Long): Cgl30? {
    return autenticacaoCache.getOrPut(cgl30autenticacao.toString()) {
      unique(
        DbQueryBuilders.build(
          Cgl30::class,
          "*, cgl30empAtiva.*",
           where = WhereAllEquals(mapOf(N_CGL30AUTENTICACAO to cgl30autenticacao))
        )
      )
   }
  }
}