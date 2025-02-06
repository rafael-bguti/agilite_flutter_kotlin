import 'dart:async';
import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const tableSelectColumnName = "isRowSelected";

class SpreadController extends FieldController<SpreadModel> {
  void Function(int rowIndex)? _onRowTap;
  final ValueNotifier<bool> loading = ValueNotifier(false);

  bool disableScroll = false;
  bool disableVerticalScroll = false;
  bool readOnly = false;
  bool showSelectColumn = true;
  List<ASpreadColumn<dynamic>> _columns = [];

  AScrollController scrollController = AScrollController();

  FutureOr<void> Function(Map<String, dynamic> row)? onAddNewRow;
  FutureOr<bool> Function()? canAddNewRow;

  void Function(SpreadController controller, int row, String columnName)? onCellStopEdit;

  FormController moreDetailFormController = FormController();

  String? labelTextToValidationMessage;

  SpreadController(
    super.name,
  ) : super(defaultValue: SpreadModel.empty()) {
    focusNode.addListener(_spreadFocusListener);
    focusNode.onKey = _onSpreadKeyListener;
  }

  @override
  void dispose() {
    for (final column in columns) {
      column.dispose();
    }
    loading.dispose();
    scrollController.dispose();
    moreDetailFormController.dispose();
    super.dispose();
  }

  final SpreadModel _value = SpreadModel.empty();

  @override
  SpreadModel get value => _value;
  @override
  set value(SpreadModel newModel) {
    final valueMap = newModel._rows.map((e) => e._dataMap).toList();
    _value.replaceAll(valueMap);

    notifyListeners();
    onValueChanged();
  }

  void fireSpreadDataChanged() {
    notifyListeners();
  }

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    if (data == null || data[name] == null) {
      _value.replaceAll([]);
      return;
    }

    fillFromList(List<Map<String, dynamic>>.from(data[name] as List).toList());
  }

  void fillFromList(List<Map<String, dynamic>> data) {
    value = SpreadModel.value(data);
  }

  void refresh() {
    notifyListeners();
  }

  @override
  List<Map<String, dynamic>> get jsonValue {
    return value.toListMap();
  }

  List<LowercaseMap> get selectedData {
    final allData = value.toListMap().map((e) => LowercaseMap.fromMap(e)).toList();
    return selectedRows.map((row) => allData[row]).toList();
  }

  @override
  String? validate() {
    value.clearValidations();

    bool columnsIsValid = true;
    for (var column in columns) {
      columnsIsValid &= column.validate();
    }
    final localStatus = columnsIsValid ? FieldStatus.valid : FieldStatus.error;
    if (localStatus != status) {
      status = localStatus;
      notifyListeners();
    }
    if (columnsIsValid) return null;

    final indexes = value.getRowsIndexesWithError();
    if (indexes.isEmpty) return null;

    final label = labelTextToValidationMessage ?? 'Lista';

    if (indexes.length == 1) {
      return '$label: existem erros na linha ${indexes[0] + 1}';
    } else {
      return '$label: existem erros nas linhas ${indexes.map((e) => e + 1).join(', ')}';
    }
  }

  //---- Manipulação das linhas ----
  void deleteSelectedRow() {
    if (readOnly || selectedCell == null) return;
    deleteRow(selectedCell!.rowIndex);
  }

  void deleteRow(int rowIndex) {
    value.removeAt(rowIndex);
    notifyListeners();
  }

  void insertRow(int index, [Map<String, dynamic>? row]) async {
    await addRow(row, index);
  }

  Future<void> addRow([Map<String, dynamic>? row, int? rowIndexToInsert]) async {
    if (readOnly) return;

    if (canAddNewRow != null) {
      final canAddRowFutureOr = canAddNewRow!.call();

      if (canAddRowFutureOr is Future<bool>) {
        final canAddRow = await canAddRowFutureOr;
        if (!canAddRow) return;
      } else {
        if (!canAddRowFutureOr) return;
      }
    }

    final addedRow = row ?? {};
    await _fireOnAddNewRow(addedRow);
    final rows = [addedRow];

    if (rowIndexToInsert != null) {
      value.insertRow(rowIndexToInsert, rows[0]);
    } else {
      value.addRow(rows[0]);
    }

    final editingCell = _findNextEditableCell(rowIndexToInsert ?? value.length - 1);
    if (editingCell == null) {
      notifyListeners();
    } else {
      selectCell(editingCell.rowIndex, editingCell.columnIndex);
    }
  }

  Future<void> _fireOnAddNewRow(Map<String, dynamic> row) async {
    if (onAddNewRow != null) {
      final rowAdded = onAddNewRow!.call(row);
      if (rowAdded is Future) {
        try {
          loading.value = true;
          await rowAdded;
        } finally {
          loading.value = false;
        }
      }
    }
  }

  //---- Edição / Seleção ----
  SelectedCell? selectedCell;
  bool get isEditing => selectedCell?.editing ?? false;
  ASpreadColumn<dynamic>? get editingColumn => selectedCell == null ? null : columns[selectedCell!.columnIndex];

  bool isEditable(int row, int column) => !readOnly && columns[column].canEdit(row);

  void editSelectedCell(String? pressedCharacter) {
    if (selectedCell == null) return;
    if (!isEditable(selectedCell!.rowIndex, selectedCell!.columnIndex)) return;

    selectedCell = selectedCell!.edit();
    final editingColumn = _getSelectedColumn();
    editingColumn?.onEdit(selectedCell!.rowIndex, pressedCharacter);
    editingColumn?.requestFocusToEdit();

    notifyListeners();
  }

  void registerOnRowTap(void Function(int rowIndex)? rowTap) {
    if (rowTap == null) return;

    if (!readOnly) throw 'onRowTap is only available for readOnly spreads.';
    _onRowTap = rowTap;
  }

  void onCellTap(int row, int column) {
    if (readOnly) {
      if (columns[column].columnConsumeRowTap) return;
      _onRowTap?.call(row);
    } else {
      if (isEditing) return;

      if (selectedCell == null || selectedCell!.rowIndex != row || selectedCell!.columnIndex != column) {
        selectCell(row, column);
      } else {
        editSelectedCell(null);
      }
    }
  }

  void selectCell(int row, int column) {
    if (readOnly) return;

    _doStopEditingWithoutNotify();
    if (row > value.length - 1) row = value.length - 1;
    _doSelectCellWithoutNotify(row, column);
    notifyListeners();
  }

  void editCell(int row, int column) {
    if (readOnly) return;

    _doSelectCellWithoutNotify(row, column);
    editSelectedCell(null);
  }

  void stopEditing() {
    _doStopEditingWithoutNotify();
    notifyListeners();
  }

  bool get isAllColumnsFixedWidth => columns.every((column) => column.width.hasFixedWidth);

  void _doStopEditingWithoutNotify() {
    if (isEditing) {
      _getSelectedColumn()?.stopEditing(selectedCell!.rowIndex);
      selectedCell = selectedCell!.stopEdit();
    }
  }

  ASpreadColumn<dynamic>? _getSelectedColumn() {
    if (selectedCell == null) return null;
    return columns[selectedCell!.columnIndex];
  }

  void _doSelectCellWithoutNotify(int row, int column) {
    selectedCell = SelectedCell(row, column);
    if (!focusNode.hasFocus) focusNode.requestFocus();
  }

  //--- Colunas ---
  set columns(List<ASpreadColumn<dynamic>> columns) {
    if (!showSelectColumn) {
      _columns = columns;
    } else {
      _columns = [
        AColumnBool(
          tableSelectColumnName,
          '',
        ).widthFixed(52),
        ...columns
      ];
    }

    for (int i = 0; i < this.columns.length; i++) {
      this.columns[i].colIndex = i;
    }
  }

  List<ASpreadColumn<dynamic>> get columns => _columns;

  bool isRowSelected(int row) {
    return showSelectColumn && value[row].getBool(tableSelectColumnName) == true;
  }

  void unselectAllRows() {
    if (!showSelectColumn) return;

    for (int i = 0; i < value.length; i++) {
      value[i][tableSelectColumnName] = false;
    }
    notifyListeners();
  }

  List<int> get selectedRows {
    if (!showSelectColumn) return [];
    List<int> selected = [];
    for (int i = 0; i < value.length; i++) {
      if (value[i].getBool(tableSelectColumnName) == true) {
        selected.add(i);
      }
    }

    return selected;
  }

  int get rowCount => value.length;
  int get selectedRowCount => selectedRows.length;

  //---- Formulario para mais detalhes -----
  SpreadMoreDetail? _moreDetail;
  void registerMoreDetail(SpreadMoreDetail? moreDetail) {
    _moreDetail = moreDetail;
  }

  void onMoreDetailTap(int row) async {
    if (_moreDetail == null) return;

    await ASideDialog.showRight(
      builder: (ctx) => _moreDetail!.buildMoreDetailBody(globalNavigatorKey.currentContext!, row, this),
      onVisible: () => _onMoreDetailDialogVisible(row),
    );

    _moreDetail!.onDialogClose(row, this);
    selectCell(row, 0);
  }

  void _onMoreDetailDialogVisible(int row) {
    _moreDetail!.showValue(row, this);
    moreDetailFormController.focusFirst();
  }

  //---- Setters e Getters ----
  double? _rowHeight;
  double get rowHeight => _rowHeight ?? 48;
  set rowHeight(double? value) {
    if (value != _rowHeight) {
      _rowHeight = value;
      notifyListeners();
    }
  }

  bool? _showRowHover;
  bool get showRowHover => _showRowHover ?? false;
  set showRowHover(bool? value) {
    if (value != _showRowHover) {
      _showRowHover = value;
      notifyListeners();
    }
  }

  //---- Actions ----
  void _spreadFocusListener() {
    if (focusNode.hasFocus) {
      if (selectedCell == null) {
        selectCell(0, 0);
        notifyListeners();
      }
    } else {
      _doStopEditingWithoutNotify();
      selectedCell = null;
      notifyListeners();
    }
  }

  //---- Key Listeners ----
  KeyEventResult _onSpreadKeyListener(FocusNode node, RawKeyEvent event) {
    if (selectedCell != null && event is RawKeyDownEvent) {
      final column = columns[selectedCell!.columnIndex];
      final result = column.processKeyPressed(event, selectedCell!.rowIndex);
      if (result != KeyEventResult.ignored) {
        return result;
      }
    }

    if (event.character != null && !isEditing) {
      editSelectedCell(event.character == ' ' ? null : event.character);
      return KeyEventResult.handled;
    } else {
      if (event is RawKeyDownEvent) {
        final handlers = {
          LogicalKeyboardKey.enter: _onEnter,
          LogicalKeyboardKey.tab: _onTab,
          LogicalKeyboardKey.f2: _onF2,
          LogicalKeyboardKey.escape: _onEscape,
          LogicalKeyboardKey.delete: _onDelete,
          LogicalKeyboardKey.keyN: () => _onAddRow(event),
          LogicalKeyboardKey.keyE: () => _onRemoveRow(event),
          LogicalKeyboardKey.keyD: () => _onMoreDetailRow(event),
          LogicalKeyboardKey.arrowUp: () => _moveSelectedCell(0),
          LogicalKeyboardKey.arrowRight: () => _moveSelectedCell(1),
          LogicalKeyboardKey.arrowDown: () => _moveSelectedCell(2),
          LogicalKeyboardKey.arrowLeft: () => _moveSelectedCell(3),
        };

        return handlers[event.logicalKey]?.call() ?? KeyEventResult.ignored;
      }
    }

    return KeyEventResult.ignored;
  }

  KeyEventResult _onMoreDetailRow(RawKeyEvent event) {
    if (selectedCell == null || _moreDetail == null) return KeyEventResult.ignored;
    if (event.isControlPressed) {
      onMoreDetailTap(selectedCell!.rowIndex);
    }
    return KeyEventResult.ignored;
  }

  KeyEventResult _onRemoveRow(RawKeyEvent event) {
    if (selectedCell == null) return KeyEventResult.ignored;

    if (event.isControlPressed) {
      doDeleteFromKeyPressed(selectedCell!.rowIndex);
      return KeyEventResult.handled;
    }

    return KeyEventResult.ignored;
  }

  void doDeleteFromKeyPressed(int row) {
    if (readOnly) return;
    int selectedRow = selectedCell?.rowIndex ?? 0;
    int selectedColumn = selectedCell?.columnIndex ?? 0;

    if (isEditing) _doStopEditingWithoutNotify();
    deleteRow(row);

    selectCell(selectedRow, selectedColumn);
  }

  KeyEventResult _onAddRow(RawKeyEvent event) {
    if (event.isControlPressed) {
      addRow();
      return KeyEventResult.handled;
    }

    return KeyEventResult.ignored;
  }

  KeyEventResult _onDelete() {
    if (isEditing) return KeyEventResult.ignored;

    final row = selectedCell?.rowIndex;
    final column = selectedCell?.columnIndex;
    if (row != null && column != null) {
      if (!isEditable(selectedCell!.rowIndex, selectedCell!.columnIndex)) return KeyEventResult.ignored;

      value.removeValue(row, columns[column].name);
    }
    notifyListeners();

    return KeyEventResult.handled;
  }

  KeyEventResult _onF2() {
    if (isEditing) return KeyEventResult.ignored;

    editSelectedCell(null);
    return KeyEventResult.handled;
  }

  KeyEventResult _onEscape() {
    if (!isEditing) return KeyEventResult.ignored;

    stopEditing();
    return KeyEventResult.handled;
  }

  KeyEventResult _onTab() {
    if (isEditing) {
      stopEditing();
      _moveToNextEditableCell();
      return KeyEventResult.handled;
    }
    return KeyEventResult.ignored;
  }

  KeyEventResult _onEnter() {
    if (isEditing || (_getSelectedColumn() != null && _getSelectedColumn() is AColumnBool)) {
      stopEditing();
      _moveToNextEditableCell();
    } else {
      editSelectedCell(null);
    }

    return KeyEventResult.handled;
  }

  void _moveToNextEditableCell() {
    final nextEditingCell = _findNextEditableCell();
    if (nextEditingCell == null) return;

    selectCell(nextEditingCell.rowIndex, nextEditingCell.columnIndex);
  }

  SelectedCell? _findNextEditableCell([int? fixedRow]) {
    if (readOnly) return null;

    int rowIndex = fixedRow ?? selectedCell?.rowIndex ?? 0;
    int colIndex = fixedRow == null ? (selectedCell?.columnIndex ?? -1) : -1;
    colIndex++; // Inicia o loop na coluna seguinte a selecionada
    int maxRowsForToFind = fixedRow == null ? 0 : 4;

    do {
      for (; colIndex < columns.length; colIndex++) {
        if (columns[colIndex].canEdit(rowIndex)) {
          return SelectedCell(rowIndex, colIndex);
        }
      }
      if (--maxRowsForToFind < 0) return null;

      rowIndex++;
      colIndex = 0;
    } while (rowIndex < value.length);
    return null;
  }

  KeyEventResult _moveSelectedCell(int position) {
    if (isEditing && position != 0 && position != 2) return KeyEventResult.ignored;
    if (readOnly) return KeyEventResult.handled;

    int row = selectedCell?.rowIndex ?? 0;
    int col = selectedCell?.columnIndex ?? 0;
    if (position == 0) {
      row--;
    } else if (position == 1) {
      col++;
    } else if (position == 2) {
      row++;
    } else if (position == 3) {
      col--;
    }
    if (row < 0) {
      row = 0;
    }
    if (col < 0) {
      col = 0;
    }
    if (row >= value.length) {
      row = value.length - 1;
    }
    if (col >= columns.length) {
      col = columns.length - 1;
    }
    selectCell(row, col);
    scrollToSelectedCell();

    return KeyEventResult.handled;
  }

  void scrollToSelectedCell() {
    if (selectedCell == null || disableVerticalScroll) return;
    double left = _getLeftPosition(selectedCell!.columnIndex);

    Rectangle<double> scrollRect = Rectangle(
      left,
      selectedCell!.rowIndex * rowHeight,
      columns[selectedCell!.columnIndex].width.scrollWidth,
      rowHeight,
    );

    scrollController.moveScrollTo(scrollRect);
  }

  double _getLeftPosition(int columnIndex) {
    if (!isAllColumnsFixedWidth) return 0;
    double left = 0;
    for (int i = 0; i < columnIndex; i++) {
      left += columns[i].width.scrollWidth;
    }
    return left;
  }
}

class SpreadModel {
  final List<SpreadRow> _rows;

  SpreadModel.empty() : _rows = [];
  SpreadModel.value(List<Map<String, dynamic>>? data) : _rows = data?.map((e) => SpreadRow(e)).toList() ?? [];

  bool every(bool Function(Map<String, dynamic> element) test) {
    return _rows.map((e) => e._data).every(test);
  }

  void clearValidations() {
    for (var row in _rows) {
      row.clearValidations();
    }
  }

  void removeValue(int row, String columnName) {
    _rows[row].remove(columnName);
  }

  void addRow(Map<String, dynamic> row) {
    _rows.add(SpreadRow(row));
  }

  void insertRow(int rowIndex, Map<String, dynamic> row) {
    _rows.insert(rowIndex, SpreadRow(row));
  }

  void replaceAll(List<Map<String, dynamic>> data) {
    _rows.clear();
    _rows.addAll(data.map((e) => SpreadRow(e)));
  }

  void removeAt(int rowIndex) {
    _rows.removeAt(rowIndex);
  }

  void clear() {
    _rows.clear();
  }

  SpreadRow operator [](int rowindex) => _rows[rowindex];
  void operator []=(int rowindex, Map<String, dynamic> values) {
    _rows[rowindex] = SpreadRow(values);
  }

  List<Map<String, dynamic>> toListMap() => _rows.map((e) => {...e._dataMap}).toList();

  int get length => _rows.length;

  bool get isEmpty => _rows.isEmpty;

  bool get isNotEmpty => _rows.isNotEmpty;

  //---- Validacao da Spread -----
  void addValidationError(int rowIndex, String columnName, String message) {
    _rows[rowIndex]._validationMessage[columnName] = message;
  }

  void removeValidationError(int rowIndex, String columnName) {
    _rows[rowIndex]._validationMessage.remove(columnName);
  }

  bool hasValidationError(int rowIndex, String columnName) {
    return _rows[rowIndex]._validationMessage.containsKey(columnName);
  }

  List<int> getRowsIndexesWithError() {
    final indexes = <int>[];
    for (var index = 0; index < _rows.length; index++) {
      if (_rows[index]._validationMessage.isEmpty) continue;
      indexes.add(index);
    }
    return indexes;
  }

  void setValueAt(int rowIndex, String columnName, dynamic value) {
    _rows[rowIndex][columnName] = value;
  }

  void setValueAtIfNull(int rowIndex, String columnName, dynamic value) {
    if (_rows[rowIndex].getDynamic(columnName) != null) return;
    _rows[rowIndex][columnName] = value;
  }

  //---- Totalização -----
  double getTotal(String columnName) {
    double total = 0;
    for (var row in _rows) {
      total += row.getDouble(columnName, 0)!;
    }
    return total;
  }
}

class SpreadRow {
  final Map<String, dynamic?> _data;
  final Map<String, String> _validationMessage = {};

  SpreadRow(Map<String, dynamic> data) : _data = data;

  // operator [](String columnName) => _data[columnName];
  operator []=(String columnName, dynamic value) {
    _data[columnName] = value;
    _removeValidationMessage(columnName);
  }

  Map<String, dynamic?> get _dataMap => _data;

  void remove(String columnName) {
    _data.remove(columnName);
    _removeValidationMessage(columnName);
  }

  void _removeValidationMessage(String columnName) {
    if (_validationMessage.containsKey(columnName)) {
      _validationMessage.remove(columnName);
    }
  }

  Map<String, dynamic> toMap() => _data;

  void clearValidations() {
    _validationMessage.clear();
  }

  //---- Get Values -----
  dynamic getDynamic(String columnName) => _data[columnName];

  double? getDouble(String columnName, [double? defaultValue]) => _data.getDouble(columnName, defaultValue);

  int? getInt(String columnName, [int? defaultValue]) => _data.getInt(columnName, defaultValue);

  String? getString(String columnName, [String? defaultValue]) => _data.getString(columnName, defaultValue);

  bool? getBool(String columnName, [bool? defaultValue]) => _data.getBool(columnName, defaultValue);

  DateTime? getDateTime(String columnName, [DateTime? defaultValue]) => _data.getDateTime(columnName, defaultValue);
}

class SelectedCell {
  final int rowIndex;
  final int columnIndex;
  final bool editing;

  SelectedCell(
    this.rowIndex,
    this.columnIndex, {
    this.editing = false,
  });

  SelectedCell edit() {
    return SelectedCell(rowIndex, columnIndex, editing: true);
  }

  SelectedCell stopEdit() {
    return SelectedCell(rowIndex, columnIndex, editing: false);
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) || other is SelectedCell && runtimeType == other.runtimeType && rowIndex == other.rowIndex && columnIndex == other.columnIndex;

  @override
  int get hashCode => rowIndex.hashCode ^ columnIndex.hashCode;

  @override
  String toString() {
    return 'SelectedCell{rowIndex: $rowIndex, columnIndex: $columnIndex, editing: $editing}';
  }
}
