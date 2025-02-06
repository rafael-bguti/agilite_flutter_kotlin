import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _durationToFocus = Duration(milliseconds: 15);

typedef CellFormatter = String Function(SpreadController controller, int row, String columnName)?;
typedef CellRenderer = Widget Function(BuildContext context, SpreadController controller, int row, String columnName, bool isFocused);

abstract class ASpreadColumn<T> {
  final bool Function(int row)? isEditable;

  Widget buildEditCell(BuildContext context, int row);
  void onEdit(int row, String? pressedChar);
  void doRequestFocusOnEdit();

  T? getValueOnStopEdit();

  bool get columnConsumeRowTap => false;

  final bool req;
  List<FieldValidator>? validators;

  final String name;
  final String? label;
  final Alignment alignment;

  int colIndex = -1;
  late SpreadController spreadController;

  AWidth _width = const AWidth.flex(1);

  final CellFormatter? cellFormatter;
  final CellRenderer? cellRenderer;

  ASpreadColumn(
    this.name,
    this.label, {
    this.alignment = Alignment.centerLeft,
    this.isEditable,
    this.validators,
    this.req = false,
    this.cellFormatter,
    this.cellRenderer,
  });

  bool canEdit(int row) => isEditable?.call(row) ?? !spreadController.readOnly;
  bool showFooter() => false;

  void requestFocusToEdit() {
    Future.delayed(_durationToFocus, doRequestFocusOnEdit);
  }

  Widget buildHeader(BuildContext context) {
    final child = Container(
      alignment: alignment,
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: buildHeaderContent(context),
      ),
    );

    return _buildCellSizeContainer(child, true);
  }

  Widget buildFooter(BuildContext context) {
    final child = Container(
      alignment: Alignment.centerRight,
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: buildFooterContent(context),
      ),
    );

    return _buildCellSizeContainer(child, false);
  }

  bool validate() {
    final localValidators = [...?validators];
    if (req) localValidators.add(Validations.isNotEmpty);
    bool isValid = true;
    if (localValidators.isNotEmpty) {
      for (int rowIndex = 0; rowIndex < spreadController.value.length; rowIndex++) {
        isValid &= _executeValidationToRow(rowIndex, localValidators);
      }
    }

    return isValid;
  }

  bool _executeValidationToRow(int rowIndex, List<FieldValidator> validators) {
    final value = spreadController.value[rowIndex].getDynamic(name);
    for (final validation in validators) {
      final error = validation(value);
      if (error != null) {
        spreadController.value.addValidationError(rowIndex, name, error);
        return false;
      }
    }

    return true;
  }

  Widget buildHeaderContent(BuildContext context) {
    return SelectableText(
      label ?? name,
      style: textTheme!.labelLarge,
    );
  }

  Widget buildFooterContent(BuildContext context) {
    return const Text('');
  }

  Widget buildCell(BuildContext context, int row) {
    final isFocused = spreadController.selectedCell?.rowIndex == row && spreadController.selectedCell?.columnIndex == colIndex;
    final isEditing = isFocused && spreadController.selectedCell?.editing == true;
    Widget cell;
    if (isEditing) {
      cell = buildEditCellWrapper(context, row);
    } else {
      cell = buildRenderCellWrapper(context, row, isFocused);
    }

    return _buildCellSizeContainer(cell, false);
  }

  Widget buildEditCellWrapper(BuildContext context, int row) {
    return Container(
      decoration: _inEditingBorderDecoration,
      padding: const EdgeInsets.all(2),
      height: spreadController.rowHeight,
      alignment: alignment,
      child: buildEditCell(context, row),
    );
  }

  Widget buildRenderCellWrapper(BuildContext context, int row, bool isFocused) {
    final decoration = getBorderDecoration(isFocused, row);

    // Widget child = canEdit(row)
    //     ? buildRenderCell(context, row, isFocused)
    //     : DefaultTextStyle(
    //         style: TextStyle(color: onSurfaceColor.withOpacity(0.65)),
    //         child: cellRenderer?.call(context, spreadController, row, name, isFocused) ?? buildRenderCell(context, row, isFocused),
    //       );

    Widget child = cellRenderer?.call(context, spreadController, row, name, isFocused) ?? buildRenderCell(context, row, isFocused);
    return GestureDetector(
      onTapDown: (_) => spreadController.onCellTap(row, colIndex),
      child: Container(
        height: spreadController.rowHeight,
        decoration: decoration,
        alignment: alignment,
        padding: const EdgeInsets.all(2),
        child: child,
      ),
    );
  }

  Widget _buildCellSizeContainer(Widget child, bool header) {
    if (width.hasFixedWidth) {
      return SizedBox(
        width: width.width,
        height: spreadController.rowHeight,
        child: child,
      );
    } else {
      return Flexible(
        flex: width.width.toInt(),
        child: SizedBox(
          height: spreadController.rowHeight,
          child: child,
        ),
      );
    }
  }

  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    final String text;
    if (cellFormatter != null) {
      text = cellFormatter!.call(spreadController, row, name);
    } else {
      text = spreadController.value[row].getString(name) ?? '';
    }

    return Text(text);
  }

  bool hasDoubleTap() => true;

  BoxDecoration getBorderDecoration(bool isFocused, int row) {
    final hasError = spreadController.value.hasValidationError(row, name);
    final isEditing = canEdit(row);
    if (isFocused) {
      if (isEditing) {
        return _editableBorderDecoration.copyWith(color: hasError ? Colors.red.withOpacity(0.3) : Colors.transparent);
      } else {
        return _uneditableBorderDecoration.copyWith(color: hasError ? Colors.red.withOpacity(0.3) : Colors.transparent);
      }
    } else {
      return hasError ? _withErrorValidationBorderDecoration : _unFocusedBorderDecoration;
    }
  }

  void stopEditing(int rowIndex) {
    spreadController.value[rowIndex][name] = getValueOnStopEdit();
    spreadController.onCellStopEdit?.call(spreadController, rowIndex, name);
  }

  KeyEventResult processKeyPressed(RawKeyEvent event, int row) {
    return KeyEventResult.ignored;
  }

  AWidth get width => _width;
  void set width(AWidth width) => _width = width;
  ASpreadColumn<T> widthFlex(int flex) {
    _width = AWidth.flex(flex);
    return this;
  }

  ASpreadColumn<T> widthFixed(double width) {
    _width = AWidth.fixed(width);
    return this;
  }

  ASpreadColumn<T> widthChar(int charCount) {
    _width = AWidth.byCharCount(charCount);
    return this;
  }

  ASpreadColumn<T> setWidth(AWidth? width) {
    _width = width ?? const AWidth.flex(1);
    return this;
  }

  final BoxDecoration _unFocusedBorderDecoration = BoxDecoration(border: Border.all(color: Colors.transparent, width: 2));
  final BoxDecoration _editableBorderDecoration = BoxDecoration(border: Border.all(color: primaryColor, width: 2));
  final BoxDecoration _inEditingBorderDecoration = BoxDecoration(
    color: backgroundColor,
    border: Border.all(
      color: primaryColor,
      width: 2,
    ),
    boxShadow: [
      BoxShadow(
        color: Colors.black.withOpacity(0.2),
        spreadRadius: 1,
        blurRadius: 1,
        offset: const Offset(0, 1),
      ),
    ],
  );

  @mustCallSuper
  void dispose() {}

  final BoxDecoration _withErrorValidationBorderDecoration = BoxDecoration(border: Border.all(color: Colors.red, width: 2), color: Colors.red.withOpacity(0.1));
  final BoxDecoration _uneditableBorderDecoration = BoxDecoration(border: Border.all(color: Colors.grey, width: 2));

  Color getBackground(BuildContext context) => _background ??= Theme.of(context).colorScheme.surface;
  Color getOnBackground(BuildContext context) => _onBackground ??= Theme.of(context).colorScheme.onSurface;

  Color? _background;
  Color? _onBackground;

  Widget? buildTextInputFromColumn(
    BuildContext context, {
    String? name,
    String? labelText,
    InputDecoration? inputDecoration,
    bool? autoFocus,
  });
}
