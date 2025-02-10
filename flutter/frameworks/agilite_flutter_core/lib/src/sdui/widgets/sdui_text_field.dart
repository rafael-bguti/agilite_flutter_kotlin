import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_text_field.g.dart';

class SduiTextField extends SduiWidget<SduiTextFieldModel> {
  @override
  SduiTextFieldModel json2Model(Map<String, dynamic> json) => SduiTextFieldModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiTextFieldModel model) {
    return ATextField.all(
      model.type,
      model.name,
      labelText: model.labelText,
      hintText: model.hintText,
      helperText: model.helperText,
      enabled: model.enabled,
      initialValue: model.convertInitialValueByType(),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiTextFieldModel extends SduiModel {
  final FieldType type;
  final String name;
  final String? labelText;
  final String? hintText;
  final String? helperText;
  final bool? enabled;
  final String? initialValue;

  SduiTextFieldModel({
    required this.type,
    required this.name,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.initialValue,
    super.id,
  });

  dynamic convertInitialValueByType() {
    if (initialValue == null) return null;
    return switch (type) {
      FieldType.string => initialValue,
      FieldType.int => initialValue!.tryParseInt(),
      FieldType.date => initialValue!.tryParseIsoDate(),
      FieldType.double => initialValue!.tryParseDouble(),
      _ => throw 'Type not supported to SduiTextField',
    };
  }

  factory SduiTextFieldModel.fromJson(Map<String, dynamic> json) => _$SduiTextFieldModelFromJson(json);
}
