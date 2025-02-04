import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnDate extends ASpreadColumn<DateTime> {
  AColumnDate(
    super.name,
    super.label, {
    super.isEditable,
    super.req,
    super.validators,
  })  : _controller = DateController(
          name,
          createdBySpreadColumn: true,
        ),
        super(alignment: Alignment.centerLeft) {
    _init();
  }

  final DateController _controller;

  void _init() {
    _controller.suffixStartAction = _onDatePickerShow;
    _controller.suffixCompleteAction = _onDatePickerHide;
  }

  @override
  dynamic valueToJson(DateTime? value) => value?.format(dateISOFormat);

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
    var value = spreadController.value[row].getDateTime(name);

    Widget child = Text(value?.format() ?? '');
    return child;
    // return Row(
    //   children: [
    //     Expanded(
    //       child: child,
    //     ),
    //     const Padding(
    //       padding: EdgeInsets.only(right: 3.0),
    //       child: Icon(Icons.calendar_today, size: 12),
    //     )
    //   ],
    // );
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

    _controller.value = spreadController.value[row].getDateTime(name);
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
    spreadController.refresh();
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
    return ATextField.date(
      name ?? this.name,
      labelText: labelText ?? label,
      decoration: inputDecoration,
      autoFocus: autoFocus,
    );
  }
}
