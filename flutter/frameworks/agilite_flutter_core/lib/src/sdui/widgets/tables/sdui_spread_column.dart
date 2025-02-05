import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import './sdui_column_model.dart';

class SduiSpreadColumn {
  static ASpreadColumn build(BuildContext context, SduiContext sduiContext, SduiColumnModel model, [bool readOnly = false]) {
    ASpreadColumn result;
    if (readOnly) {
      final cellRenderer = createSpreadCellRendererByMod(model.mod);
      final alignment = model.type == FieldMetadataType.int || model.type == FieldMetadataType.double
          ? Alignment.centerRight
          : model.type == FieldMetadataType.date
              ? Alignment.center
              : Alignment.centerLeft;

      result = AColumnReadOnly(
        model.name,
        model.label,
        cellFormatter: cellRenderer != null ? null : _createColumnFormatterToReadOnly(model),
        cellRenderer: cellRenderer,
        alignment: alignment,
      );
    } else {
      if (model.options != null) {
        result = AColumnAutocomplete.combo(
          model.name,
          model.label,
          options: model.options!,
        );
      } else {
        switch (model.type) {
          case FieldMetadataType.string:
            result = AColumnString(
              model.name,
              model.label,
              cellFormatter: createCellColumnFormatterBySduiMod(model.mod),
            );
            break;
          case FieldMetadataType.int:
            result = AColumnInt(
              model.name,
              model.label,
            );
            break;
          case FieldMetadataType.double:
            result = AColumnDouble(
              model.name,
              model.label,
            );
            break;
          case FieldMetadataType.bool:
            result = AColumnBool(
              model.name,
              model.label,
            );
            break;
          case FieldMetadataType.date:
            result = AColumnDate(
              model.name,
              model.label,
            );
            break;
          default:
            throw Exception('Type not supported');
        }
      }
    }

    result.width = model.width ?? AWidth.byCharCount(model.label?.length ?? 10);
    return result;
  }

  static CellFormatter? _createColumnFormatterToReadOnly(SduiColumnModel model) {
    if (!model.options.isNullOrEmpty) {
      return (spreadController, row, columnName) {
        final val = spreadController.value[row].getDynamic(columnName);
        if (val == null) return '';

        return model.options!.firstWhereOrNull((element) => element.jsonKey == val)?.label ?? val.toString();
      };
    }

    if (model.type == FieldMetadataType.date) {
      return (spreadController, row, columnName) => spreadController.value[row].getDateTime(columnName)?.format() ?? '';
    } else if (model.type == FieldMetadataType.double) {
      return (spreadController, row, columnName) => spreadController.value[row].getDouble(columnName)?.format() ?? '';
    }

    return createCellColumnFormatterBySduiMod(model.mod);
  }
}
