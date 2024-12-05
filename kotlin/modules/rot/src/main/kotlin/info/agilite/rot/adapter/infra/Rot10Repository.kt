package info.agilite.rot.adapter.infra

import info.agilite.boot.orm.repositories.RootRepository
import info.agilite.rot.domain.AuthenticateModel
import org.springframework.stereotype.Repository

@Repository
class Rot10Repository() : RootRepository() {
  fun findAuthenticateByUserId(rot10id: Long): AuthenticateModel? {
    return findModel("rot10id = :rot10id", mapOf("rot10id" to rot10id))
  }

  fun findAuthenticateByEmail(rot10email: String): AuthenticateModel? {
    return findModel("UPPER(rot10email) = :rot10email", mapOf("rot10email" to rot10email.uppercase()))
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


  private fun findModel(where: String, params: Map<String, Any>): AuthenticateModel? {
    return unique(
      AuthenticateModel::class,
      """ 
         SELECT *
         FROM root.rot10
         INNER JOIN root.rot01 as rot01 ON rot10.rot10contrato = rot01.rot01id
         WHERE $where
        """.trimMargin(),
      params
    )
  }
}