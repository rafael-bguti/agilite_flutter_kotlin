import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AColumnReadOnly extends ASpreadColumn<dynamic> {
  Widget Function(BuildContext context, ASpreadController spreadController, int row, bool isFocused) renderWidgetBuilder;

  AColumnReadOnly(
    super.name,
    super.label, {
    required this.renderWidgetBuilder,
  });
  @override
  bool get columnConsumeRowTap => true;

  @override
  bool canEdit(int row) {
    return false;
  }

  @override
  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    return renderWidgetBuilder(context, spreadController, row, isFocused);
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
  dynamic getValueOnStopEdit() {
    return 1;
  }

  @override
  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {}

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
