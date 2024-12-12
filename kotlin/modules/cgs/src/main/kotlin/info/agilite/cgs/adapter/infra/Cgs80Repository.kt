package info.agilite.cgs.adapter.infra

import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.CGS80_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.cgs.Cgs80
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Cgs80Repository() : RootRepository() {
  fun findByNi(cgs80ni: String): Cgs80? {
    return unique(Cgs80::class,
      """
        SELECT * FROM Cgs80
        WHERE LOWER(cgs80ni) = :cgs80ni
        AND ${defaultWhere(CGS80_METADATA)}
      """.trimIndent(),
      mapOf("cgs80ni" to cgs80ni.lowercase())
    )
  }
}