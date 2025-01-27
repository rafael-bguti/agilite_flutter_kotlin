package info.agilite.scf.infra

import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.CGS38FORMA_BOLETO
import info.agilite.shared.entities.cgs.N_CGS38_FORMA
import org.springframework.stereotype.Repository

@Repository
class Scf02PdfGeneratorRepository : RootRepository() {

  fun findScf02GerarPDF(scf02ids: List<Long>): List<Scf02GeradorPdfModel> {
    return list(
      DbQueryBuilders.build(
        Scf02GeradorPdfModel::class,
        where = WhereSimple(
          " scf02id IN (:scf02id) " +
            " AND $N_CGS38_FORMA = $CGS38FORMA_BOLETO",
          mapOf("scf02id" to scf02ids)
        )
      )
    )
  }
}

@DbTable("scf021")
@DbSimpleJoin("scf021doc, scf02forma")
class Scf02GeradorPdfModel(
  val scf02id: Long,
  val scf021remNumero: String,
  val cgs38id: Long, //TODO trocar para o objeto completo do CGS38 para gerar o acesso à API
)