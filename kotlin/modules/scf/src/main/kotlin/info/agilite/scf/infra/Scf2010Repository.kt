package info.agilite.scf.infra

import info.agilite.boot.orm.annotations.DbSimpleJoin
import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.boot.orm.where
import info.agilite.shared.entities.cgs.CGS38FORMA_BOLETO
import info.agilite.shared.entities.cgs.N_CGS38_FORMA
import info.agilite.shared.entities.scf.*
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class Scf2010Repository: RootRepository() {
  fun listarBoletosQueDevemSerEnviadosAoBanco(): List<Scf2010Dto> {
    return list(
      DbQueryBuilders.build(
        Scf2010Dto::class,
        where = where {
          and {
            default(SCF02_METADATA)
            simple(" $N_SCF02_TIPO = $SCF02TIPO_RECEBER")
            simple(" $N_CGS38_FORMA = $CGS38FORMA_BOLETO")
            simple(" $N_SCF02_LANCAMENTO IS NULL")
            simple(" $N_SCF021_REM_NUMERO IS NULL")
          }
        },
      ),
    )
  }

  fun buscarDadosBoletosParaEnviarAoBanco(scf02ids: List<Long>): List<Scf02> {
    return list(
      DbQueryBuilders.build(
        Scf02::class,
        "*, scf02forma.*, scf02forma.cgs38conta.*, scf02entidade.*",
        where = where {
          and {
            default(SCF02_METADATA)
            simple(" scf02id IN (:scf02ids)", "scf02ids", scf02ids)
          }
        },
      ),
    )
  }
}


@DbTable("scf021")
@DbSimpleJoin("RIGHT:scf021doc, scf02forma, cgs38conta, scf02entidade")
class Scf2010Dto (
  val scf02id: Long,
  val scf02dtVenc: LocalDate,
  val scf02valor: BigDecimal,

  val cgs38nome: String,

  val cgs45nome: String,

  val cgs80nome: String,
  val cgs80ni: String?,
)