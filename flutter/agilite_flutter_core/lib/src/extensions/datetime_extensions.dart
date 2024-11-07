import 'package:intl/intl.dart';

const dateBRFormat = "dd/MM/yyyy";
const dateISOFormat = "yyyy-MM-dd";

extension DateTimeExtension on DateTime {
  String formatToIso() {
    return format(dateISOFormat);
  }

  String format([String format = dateBRFormat]) {
    return DateFormat(format, 'pt-BR').format(this);
  }

  int get daysInMonth {
    return DateTime(year, month + 1, 0).day;
  }
}
