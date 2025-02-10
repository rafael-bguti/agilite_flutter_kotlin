//Modificadores de campos / ou formatadores de colunas
import 'package:agilite_flutter_core/core.dart';

import '../ui/spread/custom_renderers/badge_custom_cell_renderer.dart';
import '../ui/spread/custom_renderers/sdui_custom_cell_renderer.dart';
import '../ui/spread/custom_renderers/status_pagamento_custom_cell_renderer.dart';

const String MOD_FONE = 'fone';
const String MOD_CEP = 'cep';
const String MOD_UF = 'uf';
const String MOD_NI = 'ni';
const String MOD_NITIPO = 'niTipo';

//Modificadores que criam renderizadores de colunas
const String MOD_BADGE_RENDER = 'badge';
const String MOD_SDUI_RENDER = 'sdui';
const String MOD_REND_STATUS_DATE_RENDER = 'statusPagtoByDate';

CellFormatter? createCellColumnFormatterBySduiMod(String? mod) {
  if (mod == null) return null;

  return switch (mod) {
    MOD_FONE => (spreadController, row, columnName) => spreadController.value[row].getString(columnName)?.formatFone() ?? '',
    MOD_NI => (spreadController, row, columnName) => spreadController.value[row].getString(columnName)?.formatCpfCNPJ() ?? '',
    MOD_CEP => (spreadController, row, columnName) => spreadController.value[row].getString(columnName)?.formatCEP() ?? '',
    _ => null
  };
}

CellRenderer? createSpreadCellRendererByMod(String? mod) {
  if (mod == null) return null;

  if (mod == MOD_BADGE_RENDER) {
    return const BadgeCustomCellRenderer().render();
  }
  if (mod == MOD_SDUI_RENDER) {
    return const SduiCustomCellRenderer().render();
  }
  if (mod.startsWith(MOD_REND_STATUS_DATE_RENDER)) {
    final partes = mod.split(':');
    if (partes.length != 3) {
      throw Exception('Modificador statusPagtoByDate inválido o servidor deve fornecer no formato status_date:coluna_vcto:coluna_pgto');
    }
    return StatusPagamentoCustomCellRenderer(partes[1], partes[2]).cellRender();
  }

  return null;
}
