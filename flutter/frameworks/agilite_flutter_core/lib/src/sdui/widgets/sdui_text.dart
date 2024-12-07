import 'package:agilite_flutter_core/src/sdui/sdui_context.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'widgets.dart';

part 'sdui_text.g.dart';

class SduiText extends SduiWidget<SduiTextModel> {
  @override
  SduiTextModel json2Model(Map<String, dynamic> json) => SduiTextModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiTextModel model) {
    return Text(
      model.text,
      key: buildKey(model.id),
    );
  }
}

@JsonSerializable()
class SduiTextModel extends SduiModel {
  final String text;

  SduiTextModel({
    required this.text,
    super.id,
  });

  factory SduiTextModel.fromJson(Map<String, dynamic> json) => _$SduiTextModelFromJson(json);
}
