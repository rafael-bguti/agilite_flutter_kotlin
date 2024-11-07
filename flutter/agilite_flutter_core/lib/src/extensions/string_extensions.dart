import 'dart:convert';
import 'dart:math';

import 'package:intl/intl.dart';

extension NullStringExtensions on String? {
  bool get isNullOrBlank {
    return this == null || this!.trim().isEmpty;
  }

  bool get hasValue {
    return this != null && this!.trim().isNotEmpty;
  }
}

extension StringExtensions on String {
  bool get isTrimEmpty {
    return trim().isEmpty;
  }

  String? get nullIfEmpty {
    return isTrimEmpty ? null : this;
  }

  String? onlyNumbers() {
    return replaceAll(RegExp(r'[^\d]'), '').nullIfEmpty;
  }

  bool get isDigit {
    return RegExp(r'^[0-9]+$').hasMatch(this);
  }

  String capitalize() {
    return "${this[0].toUpperCase()}${substring(1)}";
  }

  String decapitalize() {
    return "${this[0].toLowerCase()}${substring(1)}";
  }

  String substr(int start, int length) {
    StringBuffer sb = StringBuffer();
    for (var i = start; i < min(start + length, this.length); i++) {
      sb.write(this[i]);
    }
    return sb.toString();
  }

  String substrAfterLast(String separator) {
    final index = lastIndexOf(separator);
    if (index == -1) return this;
    return substring(index + separator.length);
  }

  DateTime? tryParseIsoDate() {
    return tryParseDate('yyyy-MM-dd');
  }

  DateTime? tryParsePtBRDate() {
    return tryParseDate('dd/MM/yyyy');
  }

  DateTime? tryParseDate([String format = 'yyyy-MM-dd']) {
    if (isTrimEmpty) return null;

    try {
      return DateFormat(format).parse(this);
    } on FormatException {
      return null;
    }
  }

  int? tryParseInt() {
    if (isTrimEmpty) return null;
    return int.tryParse(this);
  }

  double? tryParsePtBRDouble([String decimalSeparator = ',', String thousandSeparator = '.']) {
    return tryParseDouble(decimalSeparator, thousandSeparator);
  }

  double? tryParseDouble([String decimalSeparator = '.', String thousandSeparator = ',']) {
    if (isTrimEmpty) return null;

    try {
      return double.tryParse(replaceAll(thousandSeparator, '').replaceAll(decimalSeparator, '.'));
    } on FormatException {
      return null;
    }
  }

  List<String> splitToList(String separator, [bool trim = true, bool removeEmpty = true]) {
    List<String> rows = split(separator);
    if (trim) rows = rows.map((e) => e.trim()).toList();
    if (removeEmpty) rows.removeWhere((element) => element.isNullOrBlank);
    return rows;
  }

  String format(String mask) {
    var r = '';
    for (var im = 0, idx = 0; im < mask.length && idx < length; im++) {
      r += mask[im] == '#' ? this[idx++] : mask[im];
    }
    return r;
  }

  String formatCpfCNPJ() {
    final onlyNumbers = this.onlyNumbers();
    if (onlyNumbers?.length == 11) {
      return onlyNumbers!.format('###.###.###-##');
    } else if (onlyNumbers?.length == 14) {
      return onlyNumbers!.format('##.###.###/####-##');
    }
    return this;
  }

  String replaceAccent() {
    return replaceAll(RegExp(r'[áàâãä]'), 'a')
        .replaceAll(RegExp(r'[éèêë]'), 'e')
        .replaceAll(RegExp(r'[íìîï]'), 'i')
        .replaceAll(RegExp(r'[óòôõö]'), 'o')
        .replaceAll(RegExp(r'[úùûü]'), 'u')
        .replaceAll(RegExp(r'[ç]'), 'c')
        .replaceAll(RegExp(r'[ÁÀÂÃÄ]'), 'A')
        .replaceAll(RegExp(r'[ÉÈÊË]'), 'E')
        .replaceAll(RegExp(r'[ÍÌÎÏ]'), 'I')
        .replaceAll(RegExp(r'[ÓÒÔÕÖ]'), 'O')
        .replaceAll(RegExp(r'[ÚÙÛÜ]'), 'U')
        .replaceAll(RegExp(r'[Ç]'), 'C');
  }

  String ecodeToBase64() {
    List<int> bytes = utf8.encode(this);
    return base64Encode(bytes);
  }
}
