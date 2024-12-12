package info.agilite.cas.application

import info.agilite.boot.observability.ObserverService
import info.agilite.boot.security.LoadUserDetailService
import info.agilite.boot.security.Role
import info.agilite.boot.security.UserDetail
import info.agilite.cas.adapter.infra.Cas29Repository
import info.agilite.cas.adapter.infra.Cas30Repository
import info.agilite.cas.adapter.infra.Cas65Repository
import info.agilite.cas.adapter.infra.UserMenuBuilder
import info.agilite.cas.domain.Cas2010Model
import info.agilite.core.exceptions.ValidationException
import info.agilite.rot.application.AutenticacaoService
import info.agilite.rot.domain.AuthenticateModel
import info.agilite.shared.entities.cas.Cas30
import org.springframework.stereotype.Service

@Service
class Cas2010Service(
  private val observerService: ObserverService,
  private val menuBuilder: UserMenuBuilder,
  private val authService: AutenticacaoService,
  private val cas30repository: Cas30Repository,
  private val cas29Repository: Cas29Repository,
  private val cas65Repository: Cas65Repository,
) : LoadUserDetailService{
  fun login(userOrEmail: String, password: String): Cas2010Model {
    val auth = authService.authenticate(userOrEmail, password)
    cas30repository.setTenant(auth.rot01tenant)
    cas30repository.invalidateCache(auth.rot10id)
    val cas30 = cas30repository.findByCas30autenticacao(auth.rot10id) ?: createInternalCas30ByRot10(auth)

    return  Cas2010Model(
      nome = cas30.cas30nome,
      email = auth.rot10email,
      empresa = cas30.cas30empAtiva.cas65nome,
      token = auth.rot10token!!,
      refreshToken = auth.rot10refreshToken!!,
      menuBuilder.buildDefaultMenu()
    )
  }

  override fun loadUserByUserId(userId: Long): UserDetail? {
    val auth = authService.loadAuthenticateByUserId(userId)

    return auth?.let {
      cas30repository.setTenant(auth.rot01tenant)
      val cas30 = cas30repository.findByCas30autenticacao(auth.rot10id) ?: createInternalCas30ByRot10(auth)

      return UserDetail(
        userId = auth.rot10id,
        empId = cas30.cas30empAtiva.cas65id,
        token = auth.rot10token ?: "",
        roles = auth.rot10roles,
        tenantId = auth.rot01tenant,
      )
    }
  }

  private fun createInternalCas30ByRot10(autenticacao: AuthenticateModel): Cas30 {
    if (!isSysAdminOrSysSupport(autenticacao)) {
      observerService.programmerAttention("Tentativa de autenticação de usuário com Rot10 criado porém sem Cas30 associado. Rot10email: ${autenticacao.rot10email} Tenant: ${autenticacao.rot01tenant}")
      throw ValidationException("Usuário não está ativo no sistema.")
    }

    return Cas30(
      autenticacao.rot10id,
      autenticacao.rot10email.substringBefore("@"),
      cas65Repository.findOrCreateDefault(),
      cas29Repository.findOrCreateDefault(),
      true
    ).also {
      cas30repository.save(it)
    }
  }

  private fun isSysAdminOrSysSupport(autenticacao: AuthenticateModel): Boolean {
    return autenticacao.rot10roles != null &&
        (autenticacao.rot10roles!!.contains(Role.SYS_ADMIN.name) ||
            autenticacao.rot10roles!!.contains(Role.SYS_SUPPORT.name))
  }
}