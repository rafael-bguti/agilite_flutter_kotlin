import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnString extends ASpreadColumn<String> {
  final int? maxLength;
  final String? Function(dynamic value)? parser;

  AColumnString(
    super.name,
    super.label, {
    super.isEditable,
    this.maxLength,
    super.req,
    super.validators,
    super.alignment,
    super.formatter,
    this.parser,
  });
  final FocusNode _editingFocusNode = FocusNode();
  final TextEditingController _editingController = TextEditingController();

  @override
  String? valueFromJson(dynamic value) {
    if (parser != null) {
      return parser!(value);
    }

    return value == null
        ? null
        : value is String?
            ? value
            : "$value";
  }

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return _getTextField();
  }

  @override
  void doRequestFocusOnEdit() {
    _editingFocusNode.requestFocus();
  }

  @override
  String? getValueOnStopEdit() {
    return _editingController.text;
  }

  @override
  void onEdit(int row, String? pressedChar) {
    final value = spreadController.value[row][name];

    _editingController.text = pressedChar ?? value?.toString() ?? '';
  }

  @override
  void dispose() {
    _editingController.dispose();
    _editingFocusNode.dispose();
    super.dispose();
  }

  TextField? _textField;
  TextField _getTextField() {
    return _textField ??= TextField(
      controller: _editingController,
      focusNode: _editingFocusNode,
      decoration: null,
      maxLength: maxLength,
    );
  }

  @override
  Widget buildTextInputFromColumn(
    BuildContext context, {
    String? name,
    String? labelText,
    InputDecoration? inputDecoration,
    bool? autoFocus,
  }) {
    return ATextField.string(
      name ?? this.name,
      labelText: labelText ?? label,
      decoration: inputDecoration,
      autoFocus: autoFocus,
    );
  }
}
