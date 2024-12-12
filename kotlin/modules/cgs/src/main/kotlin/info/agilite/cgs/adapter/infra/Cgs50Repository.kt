package info.agilite.cgs.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.*
import org.springframework.stereotype.Repository

@Repository
class Cgs50Repository() : RootRepository() {
  fun findIdByCodigo(cgs50codigo: String): Long? {
    return uniqueSingleColumn(Long::class,
      """
        SELECT cgs50id FROM Cgs50
        WHERE LOWER(cgs50codigo) = :cgs50codigo
        AND ${defaultWhere(CGS50_METADATA)}
      """.trimIndent(),
      mapOf("cgs50codigo" to cgs50codigo.lowercase())
    )
  }
}