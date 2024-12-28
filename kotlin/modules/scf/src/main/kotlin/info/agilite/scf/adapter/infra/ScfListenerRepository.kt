package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.stereotype.Repository

@Repository
class ScfListenerRepository : RootRepository() {
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