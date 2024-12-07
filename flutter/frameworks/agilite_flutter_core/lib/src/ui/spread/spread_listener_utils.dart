import 'package:agilite_flutter_core/core.dart';

class SpreadCellStopEditingAction {
  final Map<String, List<CellValueChangedHandler>> _columnHandlers = {};
  void Function(SpreadController spreadController, int row, String columnName)? handlerForAllCells;

  void putColumnHandler(String columnName, CellValueChangedHandler handler) {
    _columnHandlers[columnName] = [handler];
  }

  void putColumnHandlers(String columnName, List<CellValueChangedHandler> handlers) {
    _columnHandlers[columnName] = handlers;
  }

  void putColumnsHandler(List<String> columnName, CellValueChangedHandler handler) {
    for (var name in columnName) {
      putColumnHandler(name, handler);
    }
  }

  void putColumnsHandlers(List<String> columnName, List<CellValueChangedHandler> handlers) {
    for (var name in columnName) {
      putColumnHandlers(name, handlers);
    }
  }

  void onSpreadCellStopEditing(SpreadController spreadController, int row, String columnName) {
    final handler = _columnHandlers[columnName];
    if (handler != null && handler.isNotEmpty) {
      for (var h in handler) {
        h.onCellValueChanged(spreadController, row);
      }
    }

    handlerForAllCells?.call(spreadController, row, columnName);
  }
}

abstract class CellValueChangedHandler {
  void onCellValueChanged(SpreadController spreadController, int row);
}

class MultiplyCellsHandler implements CellValueChangedHandler {
  final String destColumnName;
  final List<String> _columnNames;

  MultiplyCellsHandler(this.destColumnName, this._columnNames);

  @override
  void onCellValueChanged(SpreadController spreadController, int row) {
    final values = _columnNames.map((name) => spreadController.value.getDouble(row, name)).toList();
    if (values.contains(null)) return;

    final result = values.reduce((value, element) => value! * element!);
    spreadController.value[row][destColumnName] = result;
  }
}

class PercentCellsHandler implements CellValueChangedHandler {
  final String destColumnName;
  final String percentColumnName;
  final String valueColumnName;

  PercentCellsHandler(this.destColumnName, this.percentColumnName, this.valueColumnName);

  @override
  void onCellValueChanged(SpreadController spreadController, int row) {
    final value = spreadController.value.getDouble(row, valueColumnName);
    final percent = spreadController.value.getDouble(row, percentColumnName);
    if (value == null || percent == null) return;

    final result = value * percent / 100;
    spreadController.value[row][destColumnName] = result;
  }
}
