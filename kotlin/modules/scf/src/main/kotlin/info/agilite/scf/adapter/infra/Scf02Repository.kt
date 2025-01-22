package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.boot.orm.where
import info.agilite.scf.adapter.dto.Scf2011RetornoDto
import info.agilite.shared.entities.cgs.CGS38FORMA_BOLETO
import info.agilite.shared.entities.cgs.N_CGS38_FORMA
import info.agilite.shared.entities.scf.*
import org.springframework.stereotype.Repository

@Repository
class Scf02Repository: RootRepository() {


  fun buscarDocumentosByScf021remNumero(scf021remNumeros: List<String>, cgs45id: Long): List<Scf2011RetornoDto>? {
    return list(
      DbQueryBuilders.build(
        Scf2011RetornoDto::class,
        "*",
        where = where {
          and {
            default(SCF02_METADATA)
            simple(" $N_SCF021_REM_NUMERO IN (:scf021remNumeros)",
              "scf021remNumeros", scf021remNumeros,
            )
            eq(N_SCF021_CONTA, cgs45id)
          }
        },
        simpleJoin = "INNER JOIN Scf021 ON scf021doc = scf02id"
      )
    )
  }
}