import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _durationToFocus = Duration(milliseconds: 15);

abstract class ASpreadColumn<T> {
  final bool Function(int row)? isEditable;

  Widget buildEditCell(BuildContext context, int row);
  void onEdit(int row, String? pressedChar);
  void doRequestFocusOnEdit();

  T? getValueOnStopEdit();
  dynamic valueToJson(T? value) => value;
  T? valueFromJson(dynamic value) => value == null ? null : value as T?;

  bool get columnConsumeRowTap => false;

  final bool req;
  List<FieldValidator>? validators;

  final String name;
  final String? label;
  final Alignment alignment;

  int colIndex = -1;
  late ASpreadController spreadController;

  AWidth _width = const AWidth.flex(1);

  ASpreadColumn(
    this.name,
    this.label, {
    this.alignment = Alignment.centerLeft,
    this.isEditable,
    this.validators,
    this.req = false,
  });

  bool canEdit(int row) => isEditable?.call(row) ?? !spreadController.readOnly;
  bool showFooter() => false;

  void requestFocusToEdit() {
    Future.delayed(_durationToFocus, doRequestFocusOnEdit);
  }

  Widget buildHeader(BuildContext context) {
    final child = Container(
      alignment: Alignment.center,
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: buildHeaderContent(context),
      ),
    );

    return _buildCellSizeContainer(child);
  }

  Widget buildFooter(BuildContext context) {
    final child = Container(
      alignment: Alignment.centerRight,
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: buildFooterContent(context),
      ),
    );

    return _buildCellSizeContainer(child);
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
    final value = spreadController.value[rowIndex][name];
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
      style: const TextStyle(fontWeight: FontWeight.bold),
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

    return _buildCellSizeContainer(cell);
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

    Widget child = canEdit(row)
        ? buildRenderCell(context, row, isFocused)
        : DefaultTextStyle(
            style: TextStyle(color: onSurfaceColor?.withOpacity(0.65)),
            child: buildRenderCell(context, row, isFocused),
          );

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

  Widget _buildCellSizeContainer(Widget child) {
    const borderDecorator = BoxDecoration(
      border: Border(
        left: BorderSide(
          color: Color(0xFFCCCCCC),
          width: 1,
        ),
      ),
    );
    if (width.hasFixedWidth) {
      return Container(
        width: width.width,
        height: spreadController.rowHeight,
        decoration: borderDecorator,
        child: child,
      );
    } else {
      return Flexible(
        flex: width.width.toInt(),
        child: Container(
          height: spreadController.rowHeight,
          decoration: borderDecorator,
          child: child,
        ),
      );
    }
  }

  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    return Text((spreadController.value[row][name])?.toString() ?? '');
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

  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {}

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
