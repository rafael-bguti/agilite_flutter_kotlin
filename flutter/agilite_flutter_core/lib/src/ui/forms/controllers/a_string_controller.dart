import 'package:flutter/services.dart';

import '../../../extensions/string_extensions.dart';
import 'form_field_controller.dart';

class AStringController extends FormFieldController<String?> {
  final List<TextInputFormatter>? _inputFormatters;

  String? Function(String text)? parseValue;
  String Function(String? value)? formatValue;

  AStringController(
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
    super.autoFocus,
    super.req,
    this.parseValue,
    this.formatValue,
    List<TextInputFormatter>? inputFormatters,
  }) : _inputFormatters = inputFormatters;

  @override
  String? parse(String text) => parseValue?.call(text) ?? text.nullIfEmpty;

  @override
  String format(String? value) => formatValue?.call(value) ?? value ?? defaultValue ?? '';

  @override
  get jsonValue => value;

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    value = data?[name] as String? ?? defaultValue;
  }

  @override
  List<TextInputFormatter>? get inputFormatters => _inputFormatters;
}
