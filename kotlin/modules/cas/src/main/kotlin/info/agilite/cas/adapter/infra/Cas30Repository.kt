package info.agilite.cas.adapter.infra

import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cas.Cas30
import info.agilite.shared.entities.cas.N_CAS30AUTENTICACAO
import org.springframework.stereotype.Repository

@Repository
class Cas30Repository : RootRepository() {
  fun findByCas30autenticacao(cas30autenticacao: Long): Cas30? {
    return unique(
      DbQueryBuilders.build(
        Cas30::class,
        "cas30nome, cas30empAtiva.cas65id, cas30empAtiva.cas65nome",
        where = WhereAllEquals(mapOf(N_CAS30AUTENTICACAO to cas30autenticacao))
      )
    )
  }
}