import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

final _dateTextInputFormatter = DateTextInputFormatter();
final _minDate = DateTime(1900);
final _maxDate = DateTime(2099, 12, 31);

class DateController extends FormFieldController<DateTime?> {
  final FocusNode calendarFocusNode = FocusNode(skipTraversal: true);

  DateController(
    super.name, {
    super.defaultValue,
    super.initialValue,
    super.enabled,
    super.labelText,
    super.helperText,
    super.hintText,
    super.obscureText,
    super.maxLines,
    super.maxLength,
    super.textEditingController,
    super.keyboardType,
    super.textInputAction,
    super.validators,
    super.req,
    super.createdBySpread,
    super.autoFocus,
  }) {
    focusNode.addListener(() {
      if (!focusNode.hasFocus) {
        onFocusLost();
      }
    });

    focusNode.onKey = (node, event) {
      if (event is RawKeyDownEvent && event.isKeyPressed(LogicalKeyboardKey.f4)) {
        _onSuffixIconPressed(globalNavigatorKey.currentContext!);
        return KeyEventResult.handled;
      }
      return KeyEventResult.ignored;
    };
  }

  void onFocusLost() {
    final completeDate = _completeDateOnFocusLost();
    value = completeDate;
  }

  @override
  DateTime? parse(String text) => text.tryParsePtBRDate();

  @override
  String format(DateTime? value) => (value ?? defaultValue)?.format() ?? '';

  @override
  get jsonValue => value?.format(dateISOFormat);

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    final val = data?[name];
    value = val == null ? defaultValue : DateTime.parse(val.toString());
  }

  @override
  List<TextInputFormatter>? get inputFormatters => <TextInputFormatter>[FilteringTextInputFormatter.digitsOnly, _dateTextInputFormatter];

  @override
  Widget? suffixIcon(BuildContext context, bool enabled) => IconButton(
        tooltip: PlatformInfo.isDesktop ? 'Selecionar data (F4)' : null,
        onPressed: !enabled ? null : () => _onSuffixIconPressed(context),
        icon: const Icon(Icons.calendar_month),
        focusNode: calendarFocusNode,
      );

  void _onSuffixIconPressed(BuildContext context) {
    suffixStartAction?.call();
    showDatePicker(
      context: context,
      locale: const Locale('pt', 'BR'),
      initialDate: value ?? DateTime.now(),
      firstDate: _minDate,
      lastDate: _maxDate,
      initialEntryMode: DatePickerEntryMode.calendarOnly,
    ).then((value) {
      if (value != null) {
        this.value = value;
        validate();
      }
      suffixCompleteAction?.call();
    });
  }

  DateTime? _completeDateOnFocusLost() {
    final text = super.text;
    if (text.isNullOrBlank) return null;

    DateTime? date;
    if (text.length == 10) {
      date = text.tryParsePtBRDate();
    } else {
      final numbers = text.onlyNumbers() ?? "";
      final hoje = DateTime.now();

      String anoStr = numbers.substr(4, 4);
      String mesStr = numbers.substr(2, 2);
      String diaStr = numbers.substr(0, 2);

      int ano = int.tryParse(anoStr) ?? hoje.year;
      int mes = int.tryParse(mesStr) ?? hoje.month;
      int dia = int.tryParse(diaStr) ?? 1;

      if (ano < 10) {
        ano += 2020;
      }
      if (ano < 100) {
        ano += 2000;
      }

      date = DateTime(ano, mes, dia);
    }
    if (date == null) return null;

    if (date.isBefore(_minDate)) {
      date = _minDate;
    }
    if (date.isAfter(_maxDate)) {
      date = _maxDate;
    }

    return date;
  }

  @override
  void dispose() {
    calendarFocusNode.dispose();
    super.dispose();
  }
}

class DateTextInputFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(TextEditingValue oldValue, TextEditingValue newValue) {
    String newValueNumbers = newValue.text.onlyNumbers() ?? "";

    var newText = StringBuffer();
    for (var i = 0; i < min(newValueNumbers.length, 8); i++) {
      if (i == 2 || i == 4) {
        newText.write('/');
      }
      newText.write(newValueNumbers[i]);
    }

    String dateString = newText.toString();
    return TextEditingValue(
      text: dateString.toString(),
      selection: TextSelection.collapsed(offset: dateString.length),
    );
  }
}
