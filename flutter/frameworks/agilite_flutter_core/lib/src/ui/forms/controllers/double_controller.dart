import 'package:flutter/services.dart';

import '../../../extensions/double_extensions.dart';
import '../../../extensions/string_extensions.dart';
import 'form_field_controller.dart';

class DoubleController extends FormFieldController<double?> {
  final int minDecimalDigits;
  final int maxDecimalDigits;
  final int maxIntegerDigits;
  final String _regex;

  DoubleController(
    super.name, {
    super.defaultValue,
    super.initialValue,
    super.enabled,
    super.labelText,
    super.helperText,
    super.hintText,
    super.obscureText,
    super.maxLines,
    super.textEditingController,
    super.keyboardType,
    super.textInputAction,
    super.validators,
    super.autoFocus,
    super.req,
    super.createdBySpread,
    this.minDecimalDigits = 2,
    this.maxDecimalDigits = 2,
    this.maxIntegerDigits = 9,
  })  : _regex = '^\\d{1,${maxIntegerDigits + maxDecimalDigits + 1}}\\,?\\d{0,$maxDecimalDigits}',
        super(
          maxLength: maxIntegerDigits + maxDecimalDigits + 1,
        ) {
    focusNode.addListener(() {
      if (focusNode.hasFocus) {
        if (createdBySpread) return;
        String? text = _buildTextNumberOnFocusGained();
        if (text != null) {
          textEditingController.text = text;
          textEditingController.selection = TextSelection(baseOffset: 0, extentOffset: textEditingController.text.length);
        }
      } else if (!focusNode.hasFocus) {
        value = _buildNumberOnFocusLost();
      }
    });
  }

  @override
  double? parse(String text) => text.isTrimEmpty ? null : text.tryParsePtBRDouble();

  @override
  String format(double? value) {
    return (value ?? defaultValue)?.format(minDecimalDigits, maxDecimalDigits, true) ?? '';
  }

  @override
  get jsonValue => value;

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    value = data?[name] as double? ?? defaultValue;
  }

  @override
  TextAlign get textAlign => TextAlign.right;

  @override
  List<TextInputFormatter>? get inputFormatters => <TextInputFormatter>[FilteringTextInputFormatter.allow(RegExp(_regex))];

  double? _buildNumberOnFocusLost() {
    if (text.isNullOrBlank) return null;
    return text.tryParsePtBRDouble();
  }

  String? _buildTextNumberOnFocusGained() {
    final text = super.text;
    if (text.isNullOrBlank) return null;
    return text.tryParsePtBRDouble()?.format(minDecimalDigits, maxDecimalDigits, false) ?? "";
  }
}
