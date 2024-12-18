package info.agilite.srf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.N_CGS18MODELOEMAIL
import info.agilite.shared.entities.srf.N_SRF01DTEMAIL
import info.agilite.shared.entities.srf.SRF01_METADATA
import info.agilite.srf.adapter.dto.Srf2060Dto
import org.springframework.stereotype.Repository

@Repository
class Srf2060Repository : RootRepository() {

  fun buscarDadosDocumentosParaEnviarEmail(): List<Srf2060Dto> {
    return list(
      DbQueryBuilders.build(
        Srf2060Dto::class,
        where = WhereSimple(
          AgiliteWhere.defaultWhere(SRF01_METADATA) +
          " AND $N_CGS18MODELOEMAIL IS NOT NULL " +
          " AND $N_SRF01DTEMAIL IS NULL "
        )
      )
    )
  }
}