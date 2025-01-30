import 'package:agilite_flutter_core/src/extensions/string_extensions.dart';

extension StringFormatterExtensions on String {
  String format(String mask) {
    var r = '';
    for (var im = 0, idx = 0; im < mask.length && idx < length; im++) {
      r += mask[im] == '#' ? this[idx++] : mask[im];
    }
    return r;
  }

  String formatCpfCNPJ() {
    final onlyNumbers = this.onlyNumbers();
    if (onlyNumbers == null) return "";
    if (onlyNumbers.length <= 11) {
      return onlyNumbers.format('###.###.###-##');
    } else if (onlyNumbers.length == 14) {
      return onlyNumbers.format('##.###.###/####-##');
    }
    return this;
  }

  String formatFone() {
    if (isNullOrBlank) return "";
    String? foneNumbers = this.onlyNumbers();
    if (foneNumbers == null) return "";

    if (foneNumbers.length == 8) {
      return '${foneNumbers.substring(0, 4)}-${foneNumbers.substring(4)}';
    } else if (foneNumbers.length == 10) {
      return '(${foneNumbers.substring(0, 2)}) ${foneNumbers.substring(2, 6)}-${foneNumbers.substring(6)}';
    } else if (foneNumbers.length == 11) {
      return '(${foneNumbers.substring(0, 2)}) ${foneNumbers.substring(2, 7)}-${foneNumbers.substring(7)}';
    } else if (foneNumbers.length > 11) {
      final fonePart = foneNumbers.substring(foneNumbers.length - 11);
      final prefix = foneNumbers.substring(0, foneNumbers.length - 11);

      return '$prefix (${fonePart.substring(0, 2)}) ${fonePart.substring(2, 7)}-${fonePart.substring(7)}';
    } else {
      return foneNumbers;
    }
  }

  String formatCEP() {
    if (isNullOrBlank) return "";
    final cepNumbers = this.onlyNumbers();
    if (cepNumbers == null) return "";

    return cepNumbers.format('#####-###');
  }
}
