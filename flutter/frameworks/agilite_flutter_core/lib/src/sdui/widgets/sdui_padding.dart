import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_padding.g.dart';

class SduiPadding extends SduiWidget<SduiPaddingModel> {
  @override
  SduiPaddingModel json2Model(Map<String, dynamic> json) => SduiPaddingModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiPaddingModel model) {
    return Padding(
      padding: EdgeInsets.only(
        top: model.top ?? 0,
        right: model.right ?? 0,
        bottom: model.bottom ?? 0,
        left: model.left ?? 0,
      ),
      child: SduiRender.renderFromJson(context, model.child!),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiPaddingModel extends SduiModel {
  final double? top;
  final double? right;
  final double? bottom;
  final double? left;
  final Map<String, dynamic> child;

  SduiPaddingModel({
    this.top,
    this.right,
    this.bottom,
    this.left,
    required this.child,
    super.id,
  });

  factory SduiPaddingModel.fromJson(Map<String, dynamic> json) => _$SduiPaddingModelFromJson(json);
}
