package info.agilite.cas.adapter.infra

import info.agilite.boot.sdui.FrontEndMenuItem

interface UserMenuBuilder {
  fun buildDefaultMenu(): List<FrontEndMenuItem>
}