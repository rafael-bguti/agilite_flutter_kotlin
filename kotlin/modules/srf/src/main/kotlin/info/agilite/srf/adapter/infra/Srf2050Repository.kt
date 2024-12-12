package info.agilite.srf.adapter.infra

import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.srf.SRF01TIPO_NOTA_FISCAL_SERVICO
import info.agilite.shared.entities.srf.SRF01_METADATA
import info.agilite.shared.events.INTEGRACAO_AGUARDANDO_O_INICIO
import info.agilite.srf.adapter.web.Srf2050EmitirDto
import org.springframework.stereotype.Repository

@Repository
class Srf2050Repository : RootRepository() {

  fun findsNFSeToExport(): List<Srf2050EmitirDto> {
    return list(
      DbQueryBuilders.build(
        Srf2050EmitirDto::class,
        "*",
        where = WhereSimple("""
          ${defaultWhere(SRF01_METADATA)}
          AND srf01tipo = $SRF01TIPO_NOTA_FISCAL_SERVICO
          AND srf01integracaoGdf = $INTEGRACAO_AGUARDANDO_O_INICIO
        """),
      )

    )
  }


}