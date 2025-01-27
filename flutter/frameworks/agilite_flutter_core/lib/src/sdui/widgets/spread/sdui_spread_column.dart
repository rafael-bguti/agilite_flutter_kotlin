import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../../sdui_context.dart';

part 'sdui_spread_column.g.dart';

class SduiSpreadColumn {
  static ASpreadColumn build(BuildContext context, SduiContext sduiContext, SduiSpreadColumnModel model) {
    ASpreadColumn result;
    if (model.options != null) {
      result = AColumnAutocomplete.combo(
        model.name!,
        model.label,
        options: model.options!,
      );
    } else {
      switch (model.type) {
        case FieldMetadataType.string:
          result = AColumnString(
            model.name!,
            model.label,
          );
          break;
        case FieldMetadataType.int:
          result = AColumnInt(
            model.name!,
            model.label,
          );
          break;
        case FieldMetadataType.double:
          result = AColumnDouble(
            model.name!,
            model.label,
          );
          break;
        case FieldMetadataType.bool:
          result = AColumnBool(
            model.name!,
            model.label,
          );
          break;
        case FieldMetadataType.date:
          result = AColumnDate(
            model.name!,
            model.label,
          );
          break;
        default:
          throw Exception('Type not supported');
      }
    }

    result.width = model.width ?? const AWidth.flex(1);
    return result;
  }
}

@JsonSerializable(createToJson: false)
class SduiSpreadColumnModel {
  final String? name;
  final String? label;
  final FieldMetadataType? type;
  final List<LocalOption>? options;
  final AWidth? width;

  SduiSpreadColumnModel({
    this.name,
    this.label,
    this.type,
    this.options,
    this.width,
  });

  factory SduiSpreadColumnModel.fromJson(Map<String, dynamic> json) => _$SduiSpreadColumnModelFromJson(json);
}
