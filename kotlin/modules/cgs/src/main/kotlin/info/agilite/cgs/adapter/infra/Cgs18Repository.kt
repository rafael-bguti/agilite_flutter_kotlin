package info.agilite.cgs.adapter.infra

import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Cgs18Repository() : RootRepository() {
  fun findByNome(cgs18nome: String): Cgs18? {
    return unique(Cgs18::class,
      """
        SELECT * FROM cgs18
        WHERE LOWER(cgs18nome) = :cgs18nome
        AND ${defaultWhere(CGS18_METADATA)}
      """.trimIndent(),
      mapOf("cgs18nome" to cgs18nome.lowercase())
    )
  }
}