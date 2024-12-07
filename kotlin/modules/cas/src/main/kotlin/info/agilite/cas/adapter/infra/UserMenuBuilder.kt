package info.agilite.cas.adapter.infra

import info.agilite.boot.autocode.FrontEndMenuItem

interface UserMenuBuilder {
  fun buildDefaultMenu(): List<FrontEndMenuItem>
}