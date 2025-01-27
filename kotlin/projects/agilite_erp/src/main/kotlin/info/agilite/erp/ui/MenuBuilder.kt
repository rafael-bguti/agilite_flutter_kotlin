package info.agilite.erp.ui

import info.agilite.boot.sdui.FrontEndMenuItem
import info.agilite.boot.sdui.SduiRequest
import info.agilite.cas.adapter.infra.UserMenuBuilder
import org.springframework.stereotype.Component

@Component
class MenuBuilder : UserMenuBuilder {
  override fun buildDefaultMenu():List<FrontEndMenuItem> {
    return listOf<FrontEndMenuItem>(
      FrontEndMenuItem.item(1, "Dashboard", "/cas2001", 0xe1b1),
      FrontEndMenuItem.group(2, "Cadastros", children = listOf(
        FrontEndMenuItem.item(21, "Entidades", "/sdui/${buildSduiRequestBase64("cgs1080")}", 0xe491),
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
        FrontEndMenuItem.item(41, "Importar Documentos", "/srf2030", 0xe491),
        FrontEndMenuItem.item(42, "Gerar NFSe", "/srf2050", 0xe491),
        FrontEndMenuItem.item(43, "Enviar boletos para o Banco", "/scf2010", 0xe491),
        FrontEndMenuItem.item(44, "Enviar email documentos", "/srf2060", 0xf523),
        FrontEndMenuItem.item(45, "Retorno dos boletos", "/scf2011", 0xf523),
      ))
    )
  }

  private fun buildSduiRequestBase64(
    name: String,
    providerClass: String? = null,
  ): String {
    return SduiRequest(name, providerClass).toBase64()
  }
}