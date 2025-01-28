import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_autocomplete.g.dart';

class SduiAutocomplete extends SduiWidget<SduiAutocompleteModel> {
  @override
  SduiAutocompleteModel json2Model(Map<String, dynamic> json) => SduiAutocompleteModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiAutocompleteModel model) {
    return AAutocompleteField.api(
      model.name,
      labelText: model.labelText,
      hintText: model.hintText,
      helperText: model.helperText,
      enabled: model.enabled,
      key: buildKey(model.id),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiAutocompleteModel extends SduiModel {
  final String name;
  final String? labelText;
  final String? hintText;
  final String? helperText;
  final bool? enabled;

  SduiAutocompleteModel({
    required this.name,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    super.id,
  });

  factory SduiAutocompleteModel.fromJson(Map<String, dynamic> json) => _$SduiAutocompleteModelFromJson(json);
}
