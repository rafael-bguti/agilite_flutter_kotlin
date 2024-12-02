package info.agilite.boot.modules.rot.adapter.web

import info.agilite.server_core.security.UserContext
import info.agilite.server_core.spring.RestMapping

@RestMapping("/teste")
class RotTesteController {

  fun teste(): String {
    return UserContext.user?.token ?: "Sem token"
  }
}