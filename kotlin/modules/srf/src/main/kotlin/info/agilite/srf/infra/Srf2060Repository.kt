package info.agilite.srf.infra

import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.boot.orm.where
import info.agilite.core.extensions.addOrReplace
import info.agilite.core.json.JsonUtils
import info.agilite.shared.entities.cgs.Cgs15
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.cgs.N_CGS18_MODELO_EMAIL
import info.agilite.shared.entities.gdf.Gdf10
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.srf.*
import info.agilite.shared.events.INTEGRACAO_NAO_EXECUTAR
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.srf.domain.Srf2060Doc
import info.agilite.srf.domain.Srf2060Filter
import info.agilite.srf.domain.Srf2060Mail
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder.simple
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class Srf2060Repository : RootRepository() {

  fun updateEmailSent(srf01: Srf01) {
    srf01.srf01dtEmail = LocalDate.now()

    //Ignora a validação de transação, pois o sistema precisa salvar essa alteração sem transação
    //para que se der erro no próximo envio de email, esse já está salvo
    ignoreNextTransactionValidation()
    updateChanges(srf01)
  }

  fun buscarDadosDocumentosParaEnviarEmail(filter: Srf2060Filter): List<Srf2060Mail> {
    var whereEmiss = WhereSimple("true")
    if(filter.emissIni != null && filter.emissFim != null) {
      whereEmiss = WhereSimple("srf01dtEmiss BETWEEN :emissIni AND :emissFim", mapOf("emissIni" to filter.emissIni, "emissFim" to filter.emissFim))
    }

    var whereReenv = WhereSimple(" $N_SRF01_DT_EMAIL IS NULL ")
    if(filter.reenviar == true){
      whereReenv = WhereSimple("true")
    }

    return list(
      DbQueryBuilders.build(
        Srf2060Mail::class,
        where = where {
          and {
            default(SRF01_METADATA)
            isNull(N_SRF01_DT_CANC)
            isNotNull(N_CGS18_MODELO_EMAIL)
            simple(" $N_SRF01_INTEGRACAO_GDF IN ($INTEGRACAO_OK, $INTEGRACAO_NAO_EXECUTAR)")
            add(whereEmiss)
            add(whereReenv)
          }
        },
        orderBy = { "ORDER BY srf01dtEmiss DESC" },
        limitQuery = " LIMIT 200 "
      )
    )
  }

  fun findDocsToSendMail(srf01ids: List<Long>): List<Srf2060Doc> {
    val sql = """
      SELECT srf01.*, srf011.*, gdf10.*, cgs15.*, cgs80.*, scf02.*
      FROM Srf01
      LEFT JOIN Srf012 ON srf012doc = srf01id
      LEFT JOIN Gdf10 ON srf01dfeAprov = gdf10id
      LEFT JOIN Scf02 ON srf012documento = scf02id
      INNER JOIN Srf011 ON srf011doc = srf01id
      INNER JOIN Cgs80 ON srf01entidade = cgs80id
      INNER JOIN Cgs18 ON srf01natureza = cgs18id
      INNER JOIN Cgs15 ON cgs18modeloEmail = cgs15id
      WHERE Srf01Id IN (:srf01ids)
      ORDER BY srf01id
    """
    val dados = listMap(sql, mapOf("srf01ids" to srf01ids))

    val result = mutableListOf<Srf2060Doc>()
    var lastSrf2060Doc: Srf2060Doc? = null
    dados.forEach {
      val srf01id = (it["srf01id"] as Number).toLong()
      if(lastSrf2060Doc?.srf01?.id != srf01id) {
        lastSrf2060Doc = Srf2060Doc(
          JsonUtils.fromMap(it, Srf01::class.java),
          mutableListOf(),
          JsonUtils.fromMap(it, Cgs80::class.java),
          if(it["gdf10id"] != null) JsonUtils.fromMap(it, Gdf10::class.java) else null,
          JsonUtils.fromMap(it, Cgs15::class.java),
          mutableListOf()
        )
        result.add(lastSrf2060Doc!!)
      }
      val srf011 = JsonUtils.fromMap(it, Srf011::class.java)
      lastSrf2060Doc!!.srf011s.addOrReplace(srf011)

      if(it["scf02id"] != null) {
        val scf02 = JsonUtils.fromMap(it, Scf02::class.java)
        lastSrf2060Doc!!.scf02s.addOrReplace(scf02)
      }
    }

    return result
  }
}