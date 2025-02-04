import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import './sdui_column_model.dart';

class SduiSpreadColumn {
  static ASpreadColumn build(BuildContext context, SduiContext sduiContext, SduiColumnModel model) {
    ASpreadColumn result;
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
            formatter: createColumnFormatterByMetadataMod(model.mod),
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

    result.width = model.width ?? AWidth.byCharCount(model.label?.length ?? 10);
    return result;
  }
}
