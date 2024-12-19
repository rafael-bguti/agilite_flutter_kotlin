package info.agilite.srf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.boot.orm.where
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.ReflectionUtils
import info.agilite.shared.entities.cgs.Cgs15
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.cgs.N_CGS18MODELOEMAIL
import info.agilite.shared.entities.gdf.Gdf10
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.srf.N_SRF01DTEMAIL
import info.agilite.shared.entities.srf.N_SRF01INTEGRACAOGDF
import info.agilite.shared.entities.srf.SRF01_METADATA
import info.agilite.shared.entities.srf.Srf01
import info.agilite.shared.events.INTEGRACAO_NAO_EXECUTAR
import info.agilite.shared.events.INTEGRACAO_OK
import info.agilite.srf.domain.Srf2060Doc
import info.agilite.srf.domain.Srf2060Mail
import org.springframework.stereotype.Repository

@Repository
class Srf2060Repository : RootRepository() {

  fun buscarDadosDocumentosParaEnviarEmail(): List<Srf2060Mail> {
    return list(
      DbQueryBuilders.build(
        Srf2060Mail::class,
        where = where {
          and {
            default(SRF01_METADATA)
            simple(" $N_CGS18MODELOEMAIL IS NOT NULL ")
            simple(" $N_SRF01INTEGRACAOGDF IN ($INTEGRACAO_OK, $INTEGRACAO_NAO_EXECUTAR)")
            simple(" $N_SRF01DTEMAIL IS NULL ")
          }
        }
      )
    )
  }

  fun findDocsToSendMail(srf01ids: List<Long>): List<Srf2060Doc> {
    val sql = """
      SELECT srf01.*, gdf10.*, cgs15.*, cgs80.*, scf02.*
      FROM Srf01
      LEFT JOIN Srf012 ON srf012doc = srf01id
      LEFT JOIN Gdf10 ON srf01dfeAprov = gdf10id
      LEFT JOIN Scf02 ON (scf02regOrigem->>'tab' = 'srf012' AND (scf02regOrigem->>'id')::BIGINT = srf012id)
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
          JsonUtils.fromMap(it, Cgs80::class.java),
          JsonUtils.fromMap(it, Gdf10::class.java),
          JsonUtils.fromMap(it, Cgs15::class.java),
          mutableListOf()
        )
        result.add(lastSrf2060Doc!!)
      }
      if(it["scf02id"] != null) {
        val scf02 = JsonUtils.fromMap(it, Scf02::class.java)
        lastSrf2060Doc!!.scf02s.add(scf02)
      }
    }

    return result
  }
}