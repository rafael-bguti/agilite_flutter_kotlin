import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnDouble extends ASpreadColumn<double> {
  final int minDecimalDigits;
  final int maxDecimalDigits;
  final int maxIntegerDigits;

  final bool showTotalOnFooter;
  AColumnDouble(
    super.name,
    super.label, {
    super.isEditable,
    this.minDecimalDigits = 2,
    this.maxDecimalDigits = 2,
    this.maxIntegerDigits = 9,
    this.showTotalOnFooter = false,
    super.req,
    super.validators,
  })  : _controller = DoubleController(
          name,
          maxIntegerDigits: maxIntegerDigits,
          createdBySpreadColumn: true,
        ),
        super(alignment: Alignment.centerRight);

  final DoubleController _controller;

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return _getTextField();
  }

  @override
  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    var value = spreadController.value[row].getDouble(name);
    return Text(value?.format(minDecimalDigits, maxDecimalDigits, true) ?? '');
  }

  @override
  void doRequestFocusOnEdit() {
    _controller.requestFocus();
  }

  @override
  double? getValueOnStopEdit() {
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
        _controller.textEditingController.text = pressedInt.toString();
        return;
      }
    }

    _controller.value = spreadController.value[row].getDouble(name);
  }

  @override
  bool showFooter() => showTotalOnFooter;

  @override
  Widget buildFooterContent(BuildContext context) {
    if (showFooter()) {
      return SelectableText(
        spreadController.value.getTotal(name).format(),
        style: const TextStyle(fontWeight: FontWeight.bold),
      );
    }
    return const SelectableText('');
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
    return ATextField.double(
      name ?? this.name,
      labelText: labelText ?? label,
      decoration: inputDecoration,
      autoFocus: autoFocus,
    );
  }
}
