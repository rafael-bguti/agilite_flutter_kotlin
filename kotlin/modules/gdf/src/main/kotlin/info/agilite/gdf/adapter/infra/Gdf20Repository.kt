package info.agilite.gdf.adapter.infra

import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Gdf20Repository() : RootRepository() {

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