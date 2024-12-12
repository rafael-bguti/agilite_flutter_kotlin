package info.agilite.gdf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.gdf.GDF20_METADATA
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Gdf2060Repository() : RootRepository() {
  fun findNFSeFromLoteToProcessar(gdf20id: Long): List<DadosSrf01ProcessarRetorno> {
    return list(
      DbQueryBuilders.build(
        DadosSrf01ProcessarRetorno::class,
        "*",
        where = WhereAllEquals(mapOf(
          "srf01loteNFSe" to gdf20id,
        ))
      )
    )
  }
}

@DbTable("GDF2060")
data class DadosSrf01ProcessarRetorno (
  val srf01id: Long,
  val srf01serie: Integer?,
  val srf01numero: Int,
)