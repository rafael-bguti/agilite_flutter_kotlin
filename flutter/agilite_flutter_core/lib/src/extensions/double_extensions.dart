import 'dart:math';

import 'package:intl/intl.dart';

extension DoubleExtensions on double {
  String format([
    int minDecimalDigits = 2,
    int maxDecimalDigits = 2,
    bool useGrouping = true,
  ]) {
    if (maxDecimalDigits < minDecimalDigits) maxDecimalDigits = minDecimalDigits;

    final thousandPart = useGrouping ? '#,##0' : '###0';
    if (maxDecimalDigits == 0) return NumberFormat(thousandPart, "pt_BR").format(this);
    final minPart = '0' * minDecimalDigits;
    final maxPart = '#' * max(maxDecimalDigits - minDecimalDigits, 0);

    return NumberFormat('$thousandPart.$minPart$maxPart', "pt_BR").format(this);
  }

  double rounded([int decimalPlaces = 2]) {
    final mod = pow(10, decimalPlaces);
    return ((this * mod).round().toDouble() / mod);
  }
}
