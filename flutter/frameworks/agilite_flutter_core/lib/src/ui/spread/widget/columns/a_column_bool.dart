import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

enum ShowOnHeader { label, checkbox, none, both }

class AColumnBool extends ASpreadColumn<bool> {
  final ShowOnHeader showOnHeader;
  AColumnBool(
    super.name,
    super.label, {
    super.isEditable,
    this.showOnHeader = ShowOnHeader.checkbox,
    super.req,
    super.validators,
  }) : super(alignment: Alignment.center);

  final _checkBoxFocus = FocusNode(skipTraversal: true);

  @override
  bool get columnConsumeRowTap => true;

  @override
  bool canEdit(int row) {
    if (isSelectColumn()) return true;
    return super.canEdit(row);
  }

  bool isSelectColumn() => tableSelectColumnName == name;

  @override
  Widget buildHeaderContent(BuildContext context) {
    bool allSelected = spreadController.value.every((element) => element[name] == true);

    onSelectAll(bool? v) {
      for (var i = 0; i < spreadController.value.length; i++) {
        spreadController.value[i][name] = v;
      }
      spreadController.refresh();
    }

    if (!spreadController.readOnly || isSelectColumn()) {
      return ASpacingRow(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          if (showOnHeader == ShowOnHeader.label || showOnHeader == ShowOnHeader.both) super.buildHeaderContent(context),
          if (spreadController.value.isNotEmpty && (showOnHeader == ShowOnHeader.checkbox || showOnHeader == ShowOnHeader.both))
            Checkbox(
              focusNode: _checkBoxFocus,
              value: allSelected,
              onChanged: onSelectAll,
            ),
        ],
      );
    }

    return super.buildHeaderContent(context);
  }

  @override
  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    bool value = _extractValue(spreadController, row);
    return Checkbox(
      focusNode: _checkBoxFocus,
      value: value,
      onChanged: canEdit(row)
          ? (v) {
              spreadController.selectCell(row, colIndex);
              spreadController.value[row][name] = v;
              spreadController.refresh();
            }
          : null,
    );
  }

  @override
  bool hasDoubleTap() => false;

  @override
  Widget buildEditCellWrapper(BuildContext context, int row) {
    return super.buildRenderCellWrapper(context, row, true);
  }

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return buildRenderCell(context, row, true);
  }

  @override
  void doRequestFocusOnEdit() {}

  @override
  bool? getValueOnStopEdit() {
    return null;
  }

  @override
  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {}

  @override
  void onEdit(int row, String? pressedChar) {}

  @override
  void dispose() {
    _checkBoxFocus.dispose();
    super.dispose();
  }

  @override
  KeyEventResult processKeyPressed(RawKeyEvent event, int row) {
    if (event.logicalKey == LogicalKeyboardKey.space) {
      _toggleValue(spreadController, row);
      return KeyEventResult.handled;
    }

    return super.processKeyPressed(event, row);
  }

  void _toggleValue(SpreadController spreadController, int row) {
    spreadController.value[row][name] = !_extractValue(spreadController, row);
    spreadController.refresh();
  }

  bool _extractValue(SpreadController spreadController, int row) {
    var value = spreadController.value[row][name];
    if (value == null) {
      return false;
    } else {
      if (value is! bool) {
        return bool.tryParse(value.toString()) ?? false;
      }
      return value;
    }
  }

  @override
  Widget buildTextInputFromColumn(
    BuildContext context, {
    String? name,
    String? labelText,
    InputDecoration? inputDecoration,
    bool? autoFocus,
  }) {
    return ABoolField(
      name ?? this.name,
      labelText: labelText ?? label,
      autoFocus: autoFocus,
    );
  }
}
