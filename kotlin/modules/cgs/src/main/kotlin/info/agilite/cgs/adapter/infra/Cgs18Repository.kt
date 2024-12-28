package info.agilite.cgs.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import org.springframework.stereotype.Repository

@Repository
class Cgs18Repository() : RootRepository() {
  fun findByCodigo(cgs18codigo: String): Cgs18? {
    return unique(Cgs18::class,
      """
        SELECT * FROM cgs18
        WHERE LOWER(cgs18codigo) = :cgs18codigo
        AND ${defaultWhere(CGS18_METADATA)}
      """.trimIndent(),
      mapOf("cgs18codigo" to cgs18codigo.lowercase())
    )
  }
}