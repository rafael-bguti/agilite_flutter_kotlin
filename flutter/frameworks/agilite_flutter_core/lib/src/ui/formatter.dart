import 'package:agilite_flutter_core/core.dart';

typedef Formatter = String Function(dynamic value)?;

typedef ColumnFormatter = String Function(SpreadRow row, String columnName)?;

Formatter? createFormatterByMetadataMod(String? mod) {
  if (mod == null) return null;
  switch (mod) {
    case MOD_FONE:
      return (value) => value == null ? '' : value.toString().formatFone();
    case MOD_NI:
      return (value) => value == null ? '' : value.toString().formatCpfCNPJ();
    case MOD_CEP:
      return (value) => value == null ? '' : value.toString().formatCEP();
    default:
      return null;
  }
}

ColumnFormatter? createColumnFormatterByMetadataMod(String? mod) {
  if (mod == null) return null;
  if (mod.startsWith(MOD_STATUS_DATE)) {
    return _StatusDateFormatter.fromMod(mod).formatter();
  }

  return switch (mod) {
    MOD_FONE => (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatFone(),
    MOD_NI => (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatCpfCNPJ(),
    MOD_CEP => (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatCEP(),
    _ => null
  };
}

class _StatusDateFormatter {
  final String colunaVcto;
  final String colunaPgto;

  _StatusDateFormatter(this.colunaVcto, this.colunaPgto);

  factory _StatusDateFormatter.fromMod(String mod) {
    final partes = mod.split(':');
    if (partes.length != 3) {
      throw Exception('Modificador status_date inválido o servidor deve fornecer no formato status_date:coluna_vcto:coluna_pgto');
    }

    return _StatusDateFormatter(partes[1], partes[2]);
  }

  ColumnFormatter formatter() {
    return (row, columnName) {
      final vcto = row[colunaVcto];
      final pgto = row[colunaPgto];
      if (vcto == null) return 'Em Aberto';
      if (pgto == null) return 'Vencido';
      return 'Pago';
    };
  }
}
