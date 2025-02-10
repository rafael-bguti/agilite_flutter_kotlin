import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_divider.g.dart';

class SduiDivider extends SduiWidget<SduiDividerModel> {
  @override
  SduiDividerModel json2Model(Map<String, dynamic> json) => SduiDividerModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiDividerModel model) {
    return model.text != null
        ? ADivider.text(
            text: model.text!,
          )
        : const ADivider.lineOnly();
  }
}

@JsonSerializable(createToJson: false)
class SduiDividerModel extends SduiModel {
  final String? text;

  SduiDividerModel({
    this.text,
    super.id,
  });

  factory SduiDividerModel.fromJson(Map<String, dynamic> json) => _$SduiDividerModelFromJson(json);
}
