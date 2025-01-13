import 'package:flutter/services.dart';

import '../../../extensions/string_extensions.dart';
import 'form_field_controller.dart';

class IntController extends FormFieldController<int?> {
  final int maxIntegerDigits;

  IntController(
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
    super.req,
    super.createdBySpreadColumn,
    super.autoFocus,
    int? maxIntegerDigits,
  })  : maxIntegerDigits = maxIntegerDigits ?? 9,
        super(maxLength: maxIntegerDigits);

  @override
  int? parse(String text) => text.isTrimEmpty ? null : int.parse(text);

  @override
  String format(int? value) => value?.toString() ?? defaultValue?.toString() ?? '';

  @override
  get jsonValue => value;
  @override
  void fillFromJson(Map<String, dynamic>? data) {
    value = data?[name] as int? ?? defaultValue;
  }

  @override
  TextAlign get textAlign => TextAlign.right;

  @override
  List<TextInputFormatter>? get inputFormatters => <TextInputFormatter>[FilteringTextInputFormatter.allow(RegExp(r'[0-9]'))];

  @override
  TextInputType? get keyboardType => super.keyboardType ?? const TextInputType.numberWithOptions(decimal: false);
}
