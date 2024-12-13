package info.agilite.gdf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.gdf.GDF20_METADATA
import info.agilite.shared.entities.gdf.Gdf20
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Gdf20Repository() : RootRepository() {

  fun delete(gdf20id: Long) {
    delete("gdf20", gdf20id)
  }

  fun findMaxNumero(): Long {
    val sql = """
      SELECT max(gdf20numero)
      FROM Gdf20
      WHERE ${AgiliteWhere.defaultWhere(GDF20_METADATA)}
    """.trimIndent()

    return uniqueSingleColumn(Long::class, sql) ?: 0
  }

  fun updateSrf01AddLote(gdf20id: Long, srf01ids: List<Long>) {
    val sql = """
      UPDATE Srf01 set 
      srf01integracaoGdf = ${INTEGRACAO_EM_ANDAMENTO},
      srf01loteNFSe = $gdf20id
      WHERE srf01id in (${srf01ids.joinToString(",")})
    """.trimIndent()

    execute(sql)
  }
}