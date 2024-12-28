package info.agilite.scf.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.core.exceptions.ValidationException
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.cgs.N_CGS80_NI
import info.agilite.shared.entities.scf.*
import org.springframework.core.annotation.MergedAnnotationPredicates.unique
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class Scf5001Repository: RootRepository() {

  fun buscarFormaRecebimentoPelaRemessa(tenantName: String, numRemessa: String): Cgs38 {
    setTenant(tenantName)

    val sql = """
      SELECT cgs38.*
      FROM Scf021
      INNER JOIN Scf02 ON scf02id = scf021doc
      INNER JOIN Cgs38 ON cgs38id = scf02forma
      WHERE $N_SCF021_REM_NUMERO = :numRemessa
    """.trimIndent()

    return unique(Cgs38::class, sql, mapOf("numRemessa" to numRemessa)) ?: throw ValidationException("Boleto não encontrado")
  }


  fun buscarDatasTitulosEmAberto(cnpj: String, tenantName: String): List<LocalDate> {
    setTenant(tenantName)
    val sql = """
      SELECT scf02dtVenc
      FROM Scf02
      INNER JOIN Cgs80 ON cgs80id = scf02entidade
      WHERE $N_SCF02_DT_VENC < :hoje AND $N_SCF02_DT_PAGTO is null 
      AND $N_CGS80_NI = :cnpj AND $N_SCF02_TIPO = $SCF02TIPO_RECEBER
    """.trimIndent()

    return listSingleColumn(
      LocalDate::class, sql, mapOf(
      "cnpj" to cnpj,
      "hoje" to LocalDate.now()
    ))
  }
}