import 'package:agilite_flutter_core/core.dart';

typedef Formatter = String Function(dynamic value)?;

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
