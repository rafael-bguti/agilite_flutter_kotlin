package info.agilite.srf.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.shared.entities.cgs.N_CGS80_NI
import info.agilite.shared.entities.scf.N_SCF02_DT_PAGTO
import info.agilite.shared.entities.scf.N_SCF02_DT_VENC
import info.agilite.shared.entities.scf.N_SCF02_TIPO
import info.agilite.shared.entities.scf.SCF02TIPO_RECEBER
import info.agilite.shared.entities.srf.*
import info.agilite.srf.adapter.dto.Srf5001Response
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class Srf5001Repository : RootRepository() {

  fun buscarUltimosTitulosCliente(cnpj: String, tenantName: String): List<Srf5001Response> {
    setTenant(tenantName)
    val ultimo6meses = LocalDate.now().minusMonths(6).withDayOfMonth(1)
    val sql = """
      SELECT cgs18codigo, srf012valor, scf02dtPagto, scf02dtVenc, srf01numero, gdf10linkPdf, scf021remNumero
      FROM Srf012
      INNER JOIN Srf01 ON srf01id = srf012doc
      INNER JOIN Cgs18 ON cgs18id = srf01natureza
      INNER JOIN Scf02 ON scf02id = srf012documento
      INNER JOIN Gdf10 ON gdf10id = srf01dfeAprov
      INNER JOIN Scf021 ON scf021doc = scf02id
      WHERE $N_SRF01_NI = :cnpj AND $N_SRF01_ES = $SRF01ES_SAIDA AND $N_SRF01_DT_EMISS >= :ultimo6meses
    """.trimIndent()

    return list(Srf5001Response::class, sql, mapOf(
      "cnpj" to cnpj,
      "ultimo6meses" to ultimo6meses
    ))
  }
}