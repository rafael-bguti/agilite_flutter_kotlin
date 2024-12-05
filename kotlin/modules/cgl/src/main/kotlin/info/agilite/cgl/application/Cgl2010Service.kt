package info.agilite.cgl.application

import info.agilite.boot.observability.ObserverService
import info.agilite.boot.security.Role
import info.agilite.cgl.adapter.infra.Cgl29Repository
import info.agilite.cgl.adapter.infra.Cgl30Repository
import info.agilite.cgl.adapter.infra.Cgl65Repository
import info.agilite.cgl.adapter.infra.UserMenuBuilder
import info.agilite.cgl.domain.Cgl2010Model
import info.agilite.cgl.domain.entities.Cgl30
import info.agilite.core.exceptions.ValidationException
import info.agilite.rot.application.AutenticacaoService
import info.agilite.rot.domain.AutenticacaoModel
import org.springframework.stereotype.Service

@Service
class Cgl2010Service(
  private val observerService: ObserverService,
  private val menuBuilder: UserMenuBuilder,
  private val autenticacaoService: AutenticacaoService,
  private val cgl30repository: Cgl30Repository,
  private val cgl29Repository: Cgl29Repository,
  private val cgl65Repository: Cgl65Repository,
) {

  fun login(usuarioOuEmail: String, senha: String): Cgl2010Model {
    val autenticacao = autenticacaoService.autenticar(usuarioOuEmail, senha)
    val cgl30 = cgl30repository.findByCgl30autenticacao(autenticacao.rot10id) ?: createInternalCas30ByRot10(autenticacao)

    return  Cgl2010Model(
      nome = cgl30.cgl30nome,
      email = autenticacao.rot10email,
      empresa = cgl30.cgl30empAtiva.toString(),
      token = autenticacao.rot10token!!,
      refreshToken = autenticacao.rot10refreshToken!!,
      menuBuilder.buildDefaultMenu()
    )
  }

  private fun createInternalCas30ByRot10(autenticacao: AutenticacaoModel): Cgl30 {
    if (!isSysAdminOrSysSupport(autenticacao)) {
      observerService.programmerAttention("Tentativa de autenticação de usuário com Rot10 criado porém sem Cas30 associado. Rot10email: ${autenticacao.rot10email} Tenant: ${autenticacao.rot01tenant}")
      throw ValidationException("Usuário não está ativo no sistema.")
    }

    return Cgl30(
      autenticacao.rot10id,
      autenticacao.rot10email.substringBefore("@"),
      cgl65Repository.findOrCreateDefault(),
      cgl29Repository.findOrCreateDefault(),
    ).also {
      cgl30repository.save(it)
    }
  }

  private fun isSysAdminOrSysSupport(autenticacao: AutenticacaoModel): Boolean {
    return autenticacao.rot10roles != null &&
        (autenticacao.rot10roles!!.contains(Role.SYS_ADMIN.name) ||
            autenticacao.rot10roles!!.contains(Role.SYS_SUPPORT.name))
  }
}