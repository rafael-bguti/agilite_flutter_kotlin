package info.agilite.rot.adapter.infra

import info.agilite.rot.domain.Rot2010Autenticacao
import info.agilite.boot.orm.repositories.RootRepository
import org.springframework.stereotype.Repository

@Repository
class Rot2010Repository() : RootRepository() {
  fun findAutenticacaoById(rot10id: Long): Rot2010Autenticacao? {
    return findRot10("rot10id = :rot10id", mapOf("rot10id" to rot10id))
  }
  fun findRot10ByEmail(rot10email: String): Rot2010Autenticacao? {
    return findRot10("UPPER(rot10email) = :rot10email", mapOf("rot10email" to rot10email.uppercase()))
  }

  fun updateTokens(rot10id: Long, rot10token: String, rot10refreshToken: String) {
    execute(
      """ 
         UPDATE root.rot10
         SET rot10token = :rot10token, rot10refreshToken = :rot10refreshToken
         WHERE rot10id = :rot10id
      """.trimMargin(),
      mapOf(
        "rot10id" to rot10id,
        "rot10token" to rot10token,
        "rot10refreshToken" to rot10refreshToken
      )
    )
  }

  fun setTenant(tenant: String) {
    execute("SET search_path TO $tenant")
  }

  private fun findRot10(where: String, params: Map<String, Any>): Rot2010Autenticacao? {
    return unique(
      Rot2010Autenticacao::class,
      """ 
         SELECT rot10.*, rot01.rot01tenant as rot01tenant
         FROM root.rot10
         INNER JOIN root.rot01 as rot01 ON rot10.rot10contrato = rot01.rot01id
         WHERE $where
        """.trimMargin(),
      params
    )
  }
}