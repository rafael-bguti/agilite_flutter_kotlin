package info.agilite.boot.modules.cas.application

import info.agilite.boot.modules.cas.adapter.infra.Cas2020Repository
import info.agilite.boot.modules.cas.domain.Cas2020Model
import info.agilite.boot.modules.rot.domain.Rot2010Autenticacao
import info.agilite.server_core.observability.ObserverService
import info.agilite.server_core.security.Role
import info.agilite.shared.exceptions.ValidationException
import org.springframework.stereotype.Service

@Service
class Cas2020Service(
  private val observerService: ObserverService,
  private val cas2020Repository: Cas2020Repository,
) {
  fun getCas30ByAuth(rot2010auth: Rot2010Autenticacao): Cas2020Model {
    return cas2020Repository.findByCas30autenticacao(rot2010auth.rot10id) ?: createInternalCas30ByRot10(rot2010auth)
  }

  private fun createInternalCas30ByRot10(rot2010auth: Rot2010Autenticacao): Cas2020Model {
    if (!isSysAdminOrSysSupport(rot2010auth)) {
      observerService.programmerAttention("Tentativa de autenticação de usuário com Rot10 criado porém sem Cas30 associado. Rot10email: ${rot2010auth.rot10email} Tenant: ${rot2010auth.rot01tenant}")
      throw ValidationException("Usuário não está ativo no sistema.")
    }

    return Cas2020Model(
      cas30autenticacao = rot2010auth.rot10id,
      cas30nome = rot2010auth.rot10email.substringBefore("@"),
      cas30empAtiva = -1,
      cas30interno = isSysAdminOrSysSupport(rot2010auth),
    ).also {
      cas2020Repository.save(it)
    }
  }

  private fun isSysAdminOrSysSupport(rot10authDto: Rot2010Autenticacao): Boolean {
    return rot10authDto.rot10roles != null &&
        (rot10authDto.rot10roles.contains(Role.SYS_ADMIN.name) ||
            rot10authDto.rot10roles.contains(Role.SYS_SUPPORT.name))
  }
}