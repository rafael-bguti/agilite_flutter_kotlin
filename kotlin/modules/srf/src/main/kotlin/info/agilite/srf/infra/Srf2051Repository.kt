package info.agilite.srf.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.Where
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.srf.*
import info.agilite.shared.events.INTEGRACAO_EM_ANDAMENTO
import org.springframework.stereotype.Repository

@Repository
class Srf2051Repository : RootRepository() {

  fun findNFSeParaProcessar(): List<Srf01> {
    return list(
      DbQueryBuilders.build(
        Srf01::class,
        "srf01id, srf01serie, srf01numero, srf01integracaoGdf, srf01dfeAprov, srf01dtEmiss, srf01nome, srf01ni, srf01vlrTotal",
        where = buildWhereNFSeProcessar(),
        orderBy = { " ORDER BY srf01id" },
      )
    )
  }

  fun findCountNFSeParaProcessar(): Int {
    val where  = buildWhereNFSeProcessar()
    return uniqueSingleColumn(
      clazz = Int::class,
      query = """
            SELECT count(*) as count
            FROM Srf01
            ${where.where("WHERE")}
          """.trimIndent(),
      params = where.params ?: emptyMap()
    ) ?: 0
  }

  private fun buildWhereNFSeProcessar(): Where {
    return WhereSimple(
      """ 
        ${AgiliteWhere.defaultWhere(SRF01_METADATA)}
        AND $N_SRF01_INTEGRACAO_GDF = $INTEGRACAO_EM_ANDAMENTO
        AND $N_SRF01_TIPO = $SRF01TIPO_NOTA_FISCAL_SERVICO
      """.trimIndent()
    )
  }
}