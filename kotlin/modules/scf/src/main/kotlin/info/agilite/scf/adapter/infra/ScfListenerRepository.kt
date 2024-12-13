package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18SCF_NA_APROVACAO_FISCAL
import info.agilite.shared.entities.srf.SRF01_METADATA
import info.agilite.shared.entities.srf.Srf01
import org.springframework.stereotype.Repository

@Repository
class ScfListenerRepository : RootRepository() {
  fun buscarSrf01GerarScfNaProvacaoFiscal(srf01ids: List<Long>): List<Srf01> {
    return list(
      DbQueryBuilders.build(
        Srf01::class,
        "*, srf01natureza.*, srf012s.*",
        where = WhereSimple(
          """
            srf01id IN (:srf01ids)
            AND cgs18scf = $CGS18SCF_NA_APROVACAO_FISCAL
          """,
          mapOf("srf01ids" to srf01ids)
        )
      )
    )
  }

  fun buscarQtdScf02Quitados(srf012Ids: List<Long>): Long {
    return uniqueSingleColumn(
      Long::class,
      """
        SELECT COUNT(*)
        FROM Scf02
        WHERE scf02lancamento IS NOT NULL
        AND scf02id IN (
          SELECT srf012documento 
          FROM Srf012 
          WHERE srf012id IN (:srf012Ids) AND srf012documento IS NOT NULL
        )
      """.trimIndent(),
      mapOf("srf012Ids" to srf012Ids)
    ) ?: 0L
  }

  fun buscarQtdScf021(srf012Ids: List<Long>): Long {
    return uniqueSingleColumn(
      Long::class,
      """
        SELECT COUNT(*)
        FROM Scf021
        WHERE scf021doc IN (
          SELECT srf012documento 
          FROM Srf012 
          WHERE srf012id IN (:srf012Ids) AND srf012documento IS NOT NULL
        ) 
        """.trimIndent(),
      mapOf("srf012Ids" to srf012Ids)
    ) ?: 0L
  }

  fun deletarScf02(srf012Ids: List<Long>) {
    execute(
      """
        DELETE FROM Scf02 
        WHERE scf02id IN (
          SELECT srf012documento 
          FROM Srf012 
          WHERE srf012id IN (:srf012Ids) AND srf012documento IS NOT NULL
        )
        """.trimIndent(),
      mapOf("srf012Ids" to srf012Ids)
    )
  }


}