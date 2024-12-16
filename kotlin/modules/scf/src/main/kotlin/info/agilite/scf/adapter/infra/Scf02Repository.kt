package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.scf.adapter.dto.Scf2010Dto
import info.agilite.shared.entities.scf.*
import org.springframework.stereotype.Repository

@Repository
class Scf02Repository: RootRepository() {
  fun localizarBoletosParaEnviarBanco(): List<Scf02> {
    return list(
      DbQueryBuilders.build(
        Scf02::class,
        "scf02dtVenc, scf02valor, scf02forma.cgs38nome, scf02forma.cgs38conta.cgs45nome, scf02entidade.cgs80nome, scf02entidade.cgs80ni",
        where = WhereSimple(
          AgiliteWhere.defaultWhere(SCF02_METADATA) +
          " AND $N_SCF02TIPO = $SCF02TIPO_RECEBER" +
          " AND $N_SCF02LANCAMENTO IS NULL"
        ),
      ),
    )
  }
}