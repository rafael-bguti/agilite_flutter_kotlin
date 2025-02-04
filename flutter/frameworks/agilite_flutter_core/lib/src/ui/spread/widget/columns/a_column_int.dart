import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnInt extends ASpreadColumn<int> {
  final int? maxIntegerDigits;
  AColumnInt(
    super.name,
    super.label, {
    super.isEditable,
    this.maxIntegerDigits,
    super.req,
    super.validators,
  })  : _controller = IntController(
          name,
          maxIntegerDigits: maxIntegerDigits,
          createdBySpreadColumn: true,
        ),
        super(alignment: Alignment.centerRight);

  final IntController _controller;

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return _getTextField();
  }

  @override
  void doRequestFocusOnEdit() {
    _controller.requestFocus();
  }

  @override
  int? getValueOnStopEdit() {
    return _controller.value;
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  void onEdit(int row, String? pressedChar) {
    if (pressedChar != null) {
      final pressedInt = int.tryParse(pressedChar);

      if (pressedInt != null) {
        _controller.value = pressedInt;
        return;
      }
    }

    _controller.value = spreadController.value[row].getInt(name);
  }

  ATextField? _textField;
  ATextField _getTextField() {
    return _textField ??= ATextField.controller(
      _controller,
      decoration: const InputDecoration(
        border: InputBorder.none,
        contentPadding: EdgeInsets.all(0),
      ),
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
    return ATextField.int(
      name ?? this.name,
      labelText: labelText ?? label,
      decoration: inputDecoration,
      autoFocus: autoFocus,
    );
  }
}
