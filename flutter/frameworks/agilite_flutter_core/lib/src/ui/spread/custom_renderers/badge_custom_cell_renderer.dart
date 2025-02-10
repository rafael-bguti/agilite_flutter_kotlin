//Renderizadores de colunas da spread
import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/ui/widgets/tags/a_tag.dart';
import 'package:flutter/material.dart';

class BadgeCustomCellRenderer {
  const BadgeCustomCellRenderer();

  CellRenderer render() {
    return (context, spreadController, row, columnName, isSelected) {
      final badgeQuery = spreadController.value[row].getString(columnName);
      final text = badgeQuery!.substrBefore("|");
      final color = badgeQuery!.substrAfterLast("|", onBackgroundColor.value.toString());

      return ATag(text: text, color: Color(color.tryParseInt() ?? onBackgroundColor.value));
    };
  }
}
