package info.agilite.srf.infra

import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.srf.SRF01_METADATA
import info.agilite.shared.entities.srf.Srf01
import org.springframework.stereotype.Repository

@Repository
class Srf01Repository : RootRepository() {
  fun findMaxNumero(): Int {
    val sql = """
     SELECT MAX(srf01numero) 
     FROM Srf01
     WHERE ${defaultWhere(SRF01_METADATA)}
    """.trimIndent()

    return uniqueSingleColumn(Long::class, sql)?.toInt() ?: 0
  }

  fun findByIds(srf01ids: List<Long>): List<Srf01> {
    return list(
      DbQueryBuilders.build(
        Srf01::class,
        "*",
        where = WhereSimple("srf01id IN (${srf01ids.joinToString()})")
      )
    )
  }

  fun findWithEntidadeByIds(srf01ids: List<Long>): List<Srf01> {
    return list(
      DbQueryBuilders.build(
        Srf01::class,
        "*, srf01entidade",
        where = WhereSimple("srf01id IN (${srf01ids.joinToString()})")
      )
    )
  }

}