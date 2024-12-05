package info.agilite.erp.ui

import info.agilite.boot.autocode.FrontEndMenuItem
import info.agilite.cgl.adapter.infra.UserMenuBuilder
import org.springframework.stereotype.Component

@Component
class MenuBuilder : UserMenuBuilder {
  override fun buildDefaultMenu():List<FrontEndMenuItem> {
    return listOf<FrontEndMenuItem>(
      FrontEndMenuItem.item(1, "Dashboard", "/cas2001", 0xe1b1),
      FrontEndMenuItem.group(2, "Cadastros", children = listOf(
        FrontEndMenuItem.item(21, "Clientes / Fornecedores", "/cgs1080", 0xe491),
        FrontEndMenuItem.item(22, "Serviços", "/cgs1050/2", 0xf180),
        FrontEndMenuItem.item(23, "Categoria financeira", "/cgs1040", 0xe19f),
        FrontEndMenuItem.item(24, "Contas de bancos", "/cgs1045", 0xf1dd),
        FrontEndMenuItem.item(25, "Notas de Serviço", "/srf1001/1/3", 0xf1dd),
      )),
      FrontEndMenuItem.group(3, "Configurações", children = listOf(
        FrontEndMenuItem.item(31, "Códigos de serviço", "/cas1090/1", 0xe491),
        FrontEndMenuItem.item(32, "CNAE", "/cas1090/2", 0xf180),
      )),
      FrontEndMenuItem.group(4, "Processos", children = listOf(
        FrontEndMenuItem.item(41, "Transmissor Fiscal", "/gdf2010", 0xe491),
      ))
    )
  }
}