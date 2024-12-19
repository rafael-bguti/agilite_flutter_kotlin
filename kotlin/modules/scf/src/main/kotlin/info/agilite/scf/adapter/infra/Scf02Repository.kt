package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.AgiliteWhere
import info.agilite.boot.orm.WhereSimple
import info.agilite.boot.orm.query.DbQueryBuilders
import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.boot.orm.where
import info.agilite.scf.adapter.dto.Scf2011RetornoDto
import info.agilite.shared.entities.cgs.CGS38FORMA_BOLETO
import info.agilite.shared.entities.cgs.N_CGS38FORMA
import info.agilite.shared.entities.scf.*
import org.springframework.stereotype.Repository

@Repository
class Scf02Repository: RootRepository() {
  fun listarBoletosQueDevemSerEnviadosAoBanco(): List<Scf02> {
    return selectBoletosParaEnviarBanco("scf02dtVenc, scf02valor, scf02forma.cgs38nome, scf02forma.cgs38conta.cgs45nome, scf02entidade.cgs80nome, scf02entidade.cgs80ni")
  }

  fun buscarDadosBoletosParaEnviarAoBanco(): List<Scf02> {
    return selectBoletosParaEnviarBanco("*, scf02forma.*, scf02forma.cgs38conta.*, scf02entidade.*")
  }

  fun buscarDocumentosByScf021remNumero(scf021remNumeros: List<String>, cgs45id: Long): List<Scf2011RetornoDto>? {
    return list(
      DbQueryBuilders.build(
        Scf2011RetornoDto::class,
        "*",
        where = where {
          and {
            default(SCF02_METADATA)
            simple(" $N_SCF021REMNUMERO IN (:scf021remNumeros)",
              "scf021remNumeros", scf021remNumeros,
            )
            simple(" $N_SCF021CONTA = $cgs45id")
          }
        },
        simpleJoin = "INNER JOIN Scf021 ON scf021doc = scf02id"
      )
    )
  }

  private fun selectBoletosParaEnviarBanco(colunas: String): List<Scf02> {
    return list(
      DbQueryBuilders.build(
        Scf02::class,
        colunas,
        where = where {
          and {
            default(SCF02_METADATA)
            simple(" $N_SCF02TIPO = $SCF02TIPO_RECEBER")
            simple(" $N_CGS38FORMA = $CGS38FORMA_BOLETO")
            simple(" $N_SCF02LANCAMENTO IS NULL")
            simple(" $N_SCF021REMNUMERO IS NULL")
          }
        },
        simpleJoin = "LEFT JOIN Scf021 ON scf021doc = scf02id"
      ),
    )
  }

}