import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import './sdui_column_model.dart';

class SduiDatatableColumn {
  static ADataTableColumn build(BuildContext context, SduiContext sduiContext, SduiColumnModel model) {
    bool numeric = model.options == null && (model.type == FieldMetadataType.int || model.type == FieldMetadataType.double);
    double? maxWidth = model.width?.hasFixedWidth == true ? model.width?.width : null;

    return ADataTableColumn(
      model.name,
      model.label ?? model.name,
      maxWidth: maxWidth,
      numeric: numeric,
      formatter: createFormatterByMetadataMod(model.mod) ?? _createFormatter(model),
    );
  }

  static Formatter? _createFormatter(SduiColumnModel model) {
    if (model.options != null) {
      return (value) {
        if (value == null) return '';
        final option = model.options!.firstWhereOrNull((element) => element.jsonKey == value);
        return option?.label ?? '';
      };
    }

    switch (model.type) {
      case FieldMetadataType.date:
        return (value) {
          if (value == null) return '';
          final DateTime? date = value.toString().tryParseIsoDate();
          return date == null ? "" : date.format();
        };

      case FieldMetadataType.double:
        return (value) {
          if (value == null) return '';
          return (value as double).format();
        };

      default:
        return null;
    }
  }
}
