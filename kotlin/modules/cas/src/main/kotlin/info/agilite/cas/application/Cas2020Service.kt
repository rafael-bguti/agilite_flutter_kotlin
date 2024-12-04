package info.agilite.cas.application

import info.agilite.cas.infra.Cas2020Repository
import info.agilite.cas.domain.Cas2020Model
import info.agilite.rot.domain.Rot2010Autenticacao
import info.agilite.boot.observability.ObserverService
import info.agilite.boot.security.Role
import info.agilite.core.exceptions.ValidationException
import info.agilite.rot.domain.entities.Rot10
import org.springframework.stereotype.Service

@Service
class Cas2020Service(
  private val observerService: ObserverService,
  private val cas2020Repository: Cas2020Repository,
) {
  fun getCas30ByAuth(rot10: Rot10): Cas2020Model {
    return cas2020Repository.findByCas30autenticacao(rot10.rot10id!!) ?: createInternalCas30ByRot10(rot10)
  }

  private fun createInternalCas30ByRot10(rot10: Rot10): Cas2020Model {
    if (!isSysAdminOrSysSupport(rot10)) {
      observerService.programmerAttention("Tentativa de autenticação de usuário com Rot10 criado porém sem Cas30 associado. Rot10email: ${rot2010auth.rot10email} Tenant: ${rot2010auth.rot01tenant}")
      throw ValidationException("Usuário não está ativo no sistema.")
    }

    return Cas2020Model(
      cas30autenticacao = rot10.rot10id!!,
      cas30nome = rot10.rot10email!!.substringBefore("@"),
      cas30empAtiva = -1,
      cas30interno = isSysAdminOrSysSupport(rot2010auth),
    ).also {
      cas2020Repository.save(it)
    }
  }

  private fun isSysAdminOrSysSupport(rot10: Rot10): Boolean {
    return rot10.rot10roles != null &&
        (rot10.rot10roles!!.contains(Role.SYS_ADMIN.name) ||
            rot10.rot10roles!!.contains(Role.SYS_SUPPORT.name))
  }
}