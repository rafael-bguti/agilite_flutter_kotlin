//Renderizadores de colunas da spread
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class SduiCustomCellRenderer {
  const SduiCustomCellRenderer();

  CellRenderer render() {
    return (context, spreadController, row, columnName, isSelected) {
      final json = spreadController.value[row].getMap(columnName);
      if (json == null) return const SizedBox();

      return SduiRender.renderFromJson(context, json);
    };
  }
}
