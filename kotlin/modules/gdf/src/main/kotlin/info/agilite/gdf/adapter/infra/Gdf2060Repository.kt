package info.agilite.gdf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.gdf.application.DadosSrf01ProcessarRetorno
import info.agilite.shared.entities.cgs.CGS18_METADATA
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.gdf.GDF20_METADATA
import info.agilite.shared.entities.gdf.Gdf20
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import org.springframework.stereotype.Repository
import java.lang.ScopedValue.where

@Repository
class Gdf2060Repository() : RootRepository() {
  fun listarLotes(): List<Gdf20> {
    return list(
      DbQueryBuilders.build(
        Gdf20::class,
        "*",
        where = WhereSimple(AgiliteWhere.defaultWhere(GDF20_METADATA)),
        orderBy = { "ORDER BY gdf20id" },
      )
    )
  }

  fun findNFSeFromLoteToProcessar(gdf20id: Long): List<DadosSrf01ProcessarRetorno> {
    return list(
      DbQueryBuilders.build(
        DadosSrf01ProcessarRetorno::class,
        "*",
        where = WhereAllEquals(mapOf(
          "srf01loteNFSe" to gdf20id,
        )),
        orderBy = { " ORDER BY srf01id" },
      )
    )
  }
}

