import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_combo_field.g.dart';

class SduiComboField extends SduiWidget<SduiComboFieldModel> {
  @override
  SduiComboFieldModel json2Model(Map<String, dynamic> json) => SduiComboFieldModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiComboFieldModel model) {
    return AComboField(
      model.name,
      options: model.options,
      labelText: model.labelText,
      hintText: model.hintText,
      helperText: model.helperText,
      enabled: model.enabled,
      key: buildKey(model.id),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiComboFieldModel extends SduiModel {
  final String name;
  final List<LocalOption> options;
  final String? labelText;
  final String? hintText;
  final String? helperText;
  final bool? enabled;

  SduiComboFieldModel({
    required this.name,
    required this.options,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    super.id,
  });

  factory SduiComboFieldModel.fromJson(Map<String, dynamic> json) => _$SduiComboFieldModelFromJson(json);
}
