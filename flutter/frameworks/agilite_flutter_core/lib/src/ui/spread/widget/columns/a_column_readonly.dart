import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnReadOnly extends ASpreadColumn<dynamic> {
  AColumnReadOnly(
    super.name,
    super.label, {
    super.cellFormatter,
    super.cellRenderer,
    super.alignment = Alignment.centerLeft,
  });
  @override
  bool get columnConsumeRowTap => false;

  @override
  bool canEdit(int row) {
    return false;
  }

  @override
  bool hasDoubleTap() => false;

  @override
  Widget buildEditCell(BuildContext context, int row) {
    return buildRenderCell(context, row, true);
  }

  @override
  void doRequestFocusOnEdit() {}

  @override
  dynamic getValueOnStopEdit() {
    return null;
  }

  @override
  void onEdit(int row, String? pressedChar) {}

  @override
  Widget? buildTextInputFromColumn(
    BuildContext context, {
    String? name,
    String? labelText,
    InputDecoration? inputDecoration,
    bool? autoFocus,
  }) {
    return null;
  }
}
