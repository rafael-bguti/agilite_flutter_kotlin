import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_tag.g.dart';

class SduiTag extends SduiWidget<SduiTagModel> {
  @override
  SduiTagModel json2Model(Map<String, dynamic> json) => SduiTagModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiTagModel model) {
    return ATag(
      text: model.text,
      color: model.color == null ? null : Color(model.color!),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiTagModel extends SduiModel {
  final String text;
  final int? color;

  SduiTagModel({
    required this.text,
    this.color,
  });

  factory SduiTagModel.fromJson(Map<String, dynamic> json) => _$SduiTagModelFromJson(json);
}
