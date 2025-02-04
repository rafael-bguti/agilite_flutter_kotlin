//Modificadores de campos / ou formatadores de colunas
import 'package:agilite_flutter_core/core.dart';

const String MOD_FONE = 'fone';
const String MOD_CEP = 'cep';
const String MOD_UF = 'uf';
const String MOD_NI = 'ni';
const String MOD_NITIPO = 'niTipo';

//Modificadores que criam renderizadores de colunas
const String REND_STATUS_DATE = 'statusPagtoByDate';

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
  if (mod.startsWith(REND_STATUS_DATE)) {
    final partes = mod.split(':');
    if (partes.length != 3) {
      throw Exception('Modificador statusPagtoByDate inválido o servidor deve fornecer no formato status_date:coluna_vcto:coluna_pgto');
    }
    return StatusPagamentoByDateRendered(partes[1], partes[2]).formatter();
  }

  return null;
}
