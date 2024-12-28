package info.agilite.rot.application

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.security.JwtService
import info.agilite.boot.security.UserDetailCache
import info.agilite.core.utils.CypherUtils
import info.agilite.rot.adapter.infra.Rot10Repository
import info.agilite.rot.domain.AuthenticateModel
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

private const val STEPS = 4
private const val JWT_MINUTES_TO_EXPIRE_TOKEN: Long = 24 * 60
private const val JWT_MINUTES_TO_EXPIRE_REFRESH_TOKEN: Long = 24 * 60 * 2

@Service
class AutenticacaoService(
  private val rot10Repository: Rot10Repository,
  private val jwtService: JwtService,
  private val userCache: UserDetailCache,
) {

  fun loadAuthenticateByUserId(userId: Long): AuthenticateModel? {
    return rot10Repository.findAuthenticateByUserId(userId)
  }

  fun authenticate(userEmail: String, password: String): AuthenticateModel {
    return rot10Repository.findAuthenticateByEmail(userEmail)?.let {
      validatePassword(password, it)
      val token = jwtService.generateToken(it.rot10id, JWT_MINUTES_TO_EXPIRE_TOKEN)
      val refreshToken = jwtService.generateToken(it.rot10id, JWT_MINUTES_TO_EXPIRE_REFRESH_TOKEN)

      rot10Repository.updateTokens(it.rot10id, token, refreshToken)
      userCache.removeUser(it.rot10id)

      it.rot10token = token
      it.rot10refreshToken = refreshToken

      it
    } ?: throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")
  }

  private fun validatePassword(password: String, autenticado: AuthenticateModel) {
    val root10senha = autenticado.rot10senha
    val cryptPassword = CypherUtils.encryptToPassword(password, STEPS)
    if (root10senha != cryptPassword) throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")
  }
}