package info.agilite.rot.application

import info.agilite.boot.exceptions.ClientException
import info.agilite.boot.security.JwtService
import info.agilite.boot.security.UserDetailCache
import info.agilite.core.extensions.encryptToPassword
import info.agilite.rot.adapter.infra.Rot2010Repository
import info.agilite.rot.domain.Rot2010Autenticacao
import info.agilite.rot.domain.Rot2010Response
import info.agilite.rot.domain.defaultMenu
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

private const val STEPS = 4
private const val JWT_MINUTES_TO_EXPIRE_TOKEN: Long = 3 * 60
private const val JWT_MINUTES_TO_EXPIRE_REFRESH_TOKEN: Long = 24 * 60 * 2

@Service
class Rot2010Service(
  private val rot2010Repository: Rot2010Repository,
  private val cas2020Service: Cas2020Service,
  private val jwtService: JwtService,
  private val userCache: UserDetailCache,
) {

  fun autenticar(userEmail: String, password: String): Rot2010Response {
    val rot10Auth = authenticate(userEmail, password)
    rot2010Repository.setTenant(rot10Auth.rot01tenant)
    val cas30 = cas2020Service.getCas30ByAuth(rot10Auth)
    val response = Rot2010Response(
      nome = cas30.cas30nome,
      email = rot10Auth.rot10email,
      empresa = cas30.cas30empAtiva.toString(),
      token = rot10Auth.rot10token!!,
      refreshToken = rot10Auth.rot10refreshToken!!,
      null
    )

    response.menu = defaultMenu
    return response
  }

  private fun authenticate(userEmail: String, password: String): Rot2010Autenticacao {
    return rot2010Repository.findRot10ByEmail(userEmail)?.let {
      validatePassword(password, it)
      val token = jwtService.generateToken(it.rot10id, JWT_MINUTES_TO_EXPIRE_TOKEN)
      val refreshToken = jwtService.generateToken(it.rot10id, JWT_MINUTES_TO_EXPIRE_REFRESH_TOKEN)

      rot2010Repository.updateTokens(it.rot10id, token, refreshToken)
      userCache.removeUser(it.rot10id)

      it.rot10token = token
      it.rot10refreshToken = refreshToken

      it
    } ?: throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")
  }

  private fun validatePassword(password: String, user: Rot2010Autenticacao) {
    val root10senha = user.rot10senha
    val cryptPassword = password.encryptToPassword(STEPS)
    if (root10senha != cryptPassword) throw ClientException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos")
  }
}