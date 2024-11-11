import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnDate extends ASpreadColumn<DateTime> {
  AColumnDate(
    super.name,
    super.label, {
    super.isEditable,
    super.req,
    super.validators,
  })  : _controller = ADateController(
          name,
          createdBySpread: true,
        ),
        super(alignment: Alignment.centerLeft) {
    _init();
  }

  final ADateController _controller;

  void _init() {
    _controller.suffixStartAction = _onDatePickerShow;
    _controller.suffixCompleteAction = _onDatePickerHide;
  }

  @override
  dynamic valueToJson(DateTime? value) => value?.format(dateISOFormat);
  @override
  DateTime? valueFromJson(dynamic value) {
    if (value == null) return null;
    if (value is DateTime) return value;

    return value.toString().tryParseIsoDate();
  }

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return _getTextField();
  }

  @override
  DateTime? getValueOnStopEdit() {
    _controller.onFocusLost();
    return _controller.value;
  }

  @override
  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    var value = spreadController.value[row][name];

    Widget child;
    if (value == null) {
      child = const Text('');
    } else {
      if (value is! DateTime) {
        value = value.toString().tryParsePtBRDate() ?? value.toString().tryParseIsoDate();
      }
      child = Text((value as DateTime?)?.format() ?? '');
    }

    return Row(
      children: [
        Expanded(
          child: child,
        ),
        const Padding(
          padding: EdgeInsets.only(right: 3.0),
          child: Icon(Icons.calendar_today, size: 12),
        )
      ],
    );
  }

  @override
  void doRequestFocusOnEdit() {
    _controller.requestFocus();
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

    var value = spreadController.value[row][name];
    if (value == null) {
      _controller.value = null;
    } else {
      if (value is! DateTime) {
        value = value.toString().tryParseIsoDate() ?? value.toString().tryParsePtBRDate();
      }
      _controller.value = (value as DateTime?);
    }
  }

  @override
  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {
    for (final row in value) {
      final cellValue = row[name];
      if (cellValue != null) {
        if (cellValue is! DateTime) {
          row[name] = cellValue.toString().tryParseIsoDate();
        }
      }
    }
  }

  SelectedCell? selectedCellOnDatePickerShow;
  void _onDatePickerShow() {
    selectedCellOnDatePickerShow = spreadController.selectedCell;
  }

  void _onDatePickerHide() {
    if (selectedCellOnDatePickerShow == null) return;
    final date = _controller.value;
    spreadController.value[selectedCellOnDatePickerShow!.rowIndex][name] = date;
    spreadController.selectCell(selectedCellOnDatePickerShow!.rowIndex, selectedCellOnDatePickerShow!.columnIndex);
    selectedCellOnDatePickerShow = null;
    spreadController.refreshUi();
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
    return ATextField.date(
      name ?? this.name,
      labelText: labelText ?? label,
      decoration: inputDecoration,
      autoFocus: autoFocus,
    );
  }
}
