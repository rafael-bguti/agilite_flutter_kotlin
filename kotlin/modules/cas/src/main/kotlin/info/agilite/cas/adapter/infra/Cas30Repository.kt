package info.agilite.cas.adapter.infra

import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.cache.EntityCache
import info.agilite.boot.orm.cache.GlobalEntityCache
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cas.Cas30
import info.agilite.shared.entities.cas.N_CAS30AUTENTICACAO
import org.springframework.stereotype.Repository

private val autenticacaoCache = EntityCache.buildByClass(Cas30::class)

@Repository
class Cas30Repository : RootRepository() {
  init {
      // TODO: gerou um loop infinito quando eu removo o Cas30 ele chama esse invalidateCache e ele chama o removeListener
//    GlobalEntityCache.addRemovalListener {
//      if (it.value is Cas30) {
//        autenticacaoCache.invalidate((it.value as Cas30).cas30autenticacao.toString())
//      }
//    }
  }

  fun invalidateCache(cas30autenticacao: Long) {
    autenticacaoCache.invalidate(cas30autenticacao.toString())
  }

  fun findByCas30autenticacao(cas30autenticacao: Long): Cas30? {
    return autenticacaoCache.getOrPut(cas30autenticacao.toString()) {
      unique(
        DbQueryBuilders.build(
          Cas30::class,
          "*, cas30empAtiva.*",
          where = WhereAllEquals(mapOf(N_CAS30AUTENTICACAO to cas30autenticacao))
        )
      )
    }
  }
}