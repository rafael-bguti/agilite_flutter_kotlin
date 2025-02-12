package info.agilite.cgs.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.*
import org.springframework.stereotype.Repository

@Repository
class Cgs38Repository() : RootRepository() {
  fun findIdByNome(cgs38nome: String): Long? {
    return uniqueSingleColumn(Long::class,
      """
        SELECT cgs38id FROM Cgs38
        WHERE LOWER(cgs38nome) = :cgs38nome
        AND ${defaultWhere(CGS38_METADATA)}
      """.trimIndent(),
      mapOf("cgs38nome" to cgs38nome.lowercase())
    )
  }

  fun findToBaixarBoletosById(cgs38id: Long): Cgs38? {
    return unique(
      DbQueryBuilders.build(
        Cgs38::class,
        "*, cgs38conta.*",
        where = WhereSimple(
          AgiliteWhere.defaultWhere(CGS38_METADATA) +
          " AND cgs38id = $cgs38id "
        ),
      )
    )
  }
}