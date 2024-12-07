import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../../sdui_context.dart';

part 'sdui_spread_column.g.dart';

class SduiSpreadColumn {
  static ASpreadColumn build(BuildContext context, SduiContext sduiContext, SduiSpreadColumnModel model) {
    SduiSpreadColumnModel localModel = _loadModel(model);

    ASpreadColumn result;
    switch (localModel.type) {
      case FieldMetadataType.string:
        result = AColumnString(
          localModel.name!,
          localModel.label,
        );
        break;
      case FieldMetadataType.int:
        result = AColumnInt(
          localModel.name!,
          localModel.label,
        );
        break;
      case FieldMetadataType.double:
        result = AColumnDouble(
          localModel.name!,
          localModel.label,
        );
        break;
      case FieldMetadataType.bool:
        result = AColumnBool(
          localModel.name!,
          localModel.label,
        );
        break;
      case FieldMetadataType.date:
        result = AColumnDate(
          localModel.name!,
          localModel.label,
        );
        break;
      default:
        throw Exception('Type not supported');
    }

    result.width = localModel.width ?? const AWidth.flex(1);

    return result;
  }

  static SduiSpreadColumnModel _loadModel(SduiSpreadColumnModel model) {
    final SduiSpreadColumnModel hidratedModel;
    if (model.name != null) {
      hidratedModel = model;
    } else {
      final field = metadataRepository.field(model.fieldMetadataName!);
      hidratedModel = SduiSpreadColumnModel(
        name: field.name,
        label: field.label,
        type: field.type,
        width: model.width,
      );
    }

    return hidratedModel;
  }
}

@JsonSerializable()
class SduiSpreadColumnModel {
  final String? fieldMetadataName;

  final String? name;
  final String? label;
  final FieldMetadataType? type;
  final AWidth? width;

  SduiSpreadColumnModel({
    this.fieldMetadataName,
    this.name,
    this.label,
    this.type,
    this.width,
  }) : assert(fieldMetadataName != null || name != null, 'fieldMetadataName or name must be provided');

  factory SduiSpreadColumnModel.fromJson(Map<String, dynamic> json) => _$SduiSpreadColumnModelFromJson(json);
}
