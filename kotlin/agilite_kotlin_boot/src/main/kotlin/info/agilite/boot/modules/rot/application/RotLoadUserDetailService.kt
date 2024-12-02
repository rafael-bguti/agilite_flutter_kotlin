package info.agilite.boot.modules.rot.application

import info.agilite.boot.modules.cas.application.Cas2020Service
import info.agilite.boot.modules.rot.adapter.infra.Rot2010Repository
import info.agilite.server_core.security.LoadUserDetailService
import info.agilite.server_core.security.UserDetail
import org.springframework.stereotype.Service

@Service
class RotLoadUserDetailService(
  val repository: Rot2010Repository,
  val cas2020Service: Cas2020Service,
): LoadUserDetailService {

  override fun loadUserByUserId(userId: Long): UserDetail? {
    val rot2010Autenticacao = repository.findAutenticacaoById(userId) ?: return null
    repository.setTenant(rot2010Autenticacao.rot01tenant)

    val cas30 = cas2020Service.getCas30ByAuth(rot2010Autenticacao)

    return UserDetail(
      userId = rot2010Autenticacao.rot10id,
      empId = cas30.cas30empAtiva,
      tenantId = rot2010Autenticacao.rot01tenant,
      token = rot2010Autenticacao.rot10token ?: "",
      roles = rot2010Autenticacao.rot10roles,
    )
  }
}