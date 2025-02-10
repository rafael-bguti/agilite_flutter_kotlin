import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_metadata_field.g.dart';

class SduiMetadataField extends SduiWidget<SduiMetadataFieldModel> {
  @override
  SduiMetadataFieldModel json2Model(Map<String, dynamic> json) => SduiMetadataFieldModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiMetadataFieldModel model) {
    return AMetadataField(
      model.name,
      labelText: model.labelText,
      hintText: model.hintText,
      helperText: model.helperText,
      enabled: model.enabled,
      mod: model.mod,
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiMetadataFieldModel extends SduiModel {
  final String name;
  final String? labelText;
  final String? hintText;
  final String? helperText;
  final bool? enabled;
  final String? mod;

  SduiMetadataFieldModel({
    required this.name,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.mod,
    super.id,
  });

  factory SduiMetadataFieldModel.fromJson(Map<String, dynamic> json) => _$SduiMetadataFieldModelFromJson(json);
}
