package info.agilite.srf.adapter.web

import info.agilite.boot.spring.RestMapping
import info.agilite.core.extensions.format
import info.agilite.core.utils.CypherUtils
import info.agilite.srf.adapter.dto.Srf5001Response
import info.agilite.srf.infra.Srf5001Repository
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

private const val DEFAULT_MULTINFE_KEY = "uRpGExPNQIwCaz2USzhnb3dWP6qgKc9w"//TODO passar para um campo no banco por integração (banco de plugins)
@RestMapping("/public/srf5001")//TODO adicionar segurança com autenticação OAuth
class Srf5001Controller(
  private val srf5001Repository: Srf5001Repository,
) {

  @PostMapping("/{tenant}/{cnpjKey}", consumes = ["*/*"])
  fun listarDocumentosEmpresa(@PathVariable tenant: String, @PathVariable cnpjKey: String): List<Srf5001Response> {
    val cnpj = CypherUtils.decryptAES(cnpjKey, DEFAULT_MULTINFE_KEY)
    return srf5001Repository.buscarUltimosTitulosCliente(cnpj, tenant)
  }
}