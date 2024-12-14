package info.agilite.srf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.BatchOperations
import info.agilite.boot.orm.WhereAllEquals
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.srf.*
import info.agilite.shared.events.INTEGRACAO_AGUARDANDO_O_INICIO
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import info.agilite.srf.adapter.web.Srf2050EmitirDto
import org.springframework.stereotype.Repository

@Repository
class Srf2051Repository : RootRepository() {

  fun findNFSeParaProcessar(): List<Srf01> {
    return list(
      DbQueryBuilders.build(
        Srf01::class,
        "srf01id, srf01serie, srf01numero, srf01integracaoGdf, srf01dfeAprov, srf01nome, srf01ni, srf01vlrTotal",
        where = WhereSimple(
          """ 
            ${AgiliteWhere.defaultWhere(SRF01_METADATA)}
            AND $N_SRF01INTEGRACAOGDF = $INTEGRACAO_EM_ANDAMENTO
            AND $N_SRF01TIPO = $SRF01TIPO_NOTA_FISCAL_SERVICO
          """.trimIndent()
        ),
        orderBy = { " ORDER BY srf01id" },
      )
    )
  }
}