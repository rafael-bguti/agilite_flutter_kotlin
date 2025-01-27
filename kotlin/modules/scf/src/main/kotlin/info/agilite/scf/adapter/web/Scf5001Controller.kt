package info.agilite.scf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.core.extensions.format
import info.agilite.core.utils.CypherUtils
import info.agilite.scf.infra.Scf5001Repository
import info.agilite.scf.application.Scf5001Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

private const val DEFAULT_MULTINFE_KEY = "uRpGExPNQIwCaz2USzhnb3dWP6qgKc9w"//TODO passar para um campo no banco por integração (banco de plugins)
@RestMapping("/public/scf5001")//TODO adicionar segurança com autenticação OAuth
class Scf5001Controller(
  private val scf5001Service: Scf5001Service,
  private val scf5001Repository: Scf5001Repository,
) {

  @GetMapping("/{tenant}/{numRemessaKey}", consumes = ["*/*"], produces = ["application/pdf"])
  fun gerarBoleto(@PathVariable tenant: String, @PathVariable numRemessaKey: String): ByteArray {
    val numRemessa = CypherUtils.decryptAES(numRemessaKey, DEFAULT_MULTINFE_KEY)
    return scf5001Service.gerarBoleto(tenant, numRemessa)
  }

  @PostMapping("/vencidos/{tenant}/{cnpjKey}", consumes = ["*/*"])
  fun buscarTitulosVencidos(@PathVariable tenant: String, @PathVariable cnpjKey: String): String? {
    val cnpj = CypherUtils.decryptAES(cnpjKey, DEFAULT_MULTINFE_KEY)
    val datas = scf5001Repository.buscarDatasTitulosEmAberto(cnpj, tenant)
    if(datas.isEmpty()) return null

    return datas.joinToString("|") { it.format("yyyyMMdd") }
  }
}