package info.agilite.boot.modules.cas.domain

import info.agilite.server_core.orm.annotations.DbTable


@DbTable("cas30")
data class Cas2020Model(
  val cas30id: Long? = null,
  val cas30autenticacao: Long,
  val cas30nome: String,
  val cas30empAtiva: Long,
  val cas30interno: Boolean,
)