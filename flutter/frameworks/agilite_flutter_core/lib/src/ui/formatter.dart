import 'package:agilite_flutter_core/core.dart';

typedef Formatter = String Function(dynamic value)?;

typedef ColumnFormatter = String Function(Map<String, dynamic> row, String columnName)?;

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
  switch (mod) {
    case MOD_FONE:
      return (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatFone();
    case MOD_NI:
      return (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatCpfCNPJ();
    case MOD_CEP:
      return (row, columnName) => row[columnName] == null ? '' : row[columnName].toString().formatCEP();
    case MOD_STATUS_DATE:
      return (row, columnName) => row[columnName]?.toString() ?? 'Em Aberto';
    default:
      return null;
  }
}
