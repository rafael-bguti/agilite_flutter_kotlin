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
  })  : _controller = AIntController(
          name,
          maxIntegerDigits: maxIntegerDigits,
          createdBySpread: true,
        ),
        super(alignment: Alignment.centerRight);

  final AIntController _controller;

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

    var value = spreadController.value[row][name];
    if (value == null) {
      _controller.value = null;
    } else {
      if (value is! int) {
        value = int.tryParse(value.toString());
      }
      _controller.value = value as int?;
    }
  }

  @override
  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {
    for (final row in value) {
      final cellValue = row[name];
      if (cellValue != null) {
        if (cellValue is! int) {
          row[name] = int.tryParse(cellValue.toString());
        }
      }
    }
  }

  ATextField? _textField;
  ATextField _getTextField() {
    return _textField ??= ATextField.controller(
      _controller,
      decoration: const InputDecoration(
        border: InputBorder.none,
        isDense: true,
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
