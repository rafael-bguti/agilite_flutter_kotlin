import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_center.g.dart';

class SduiCenter extends SduiWidget<SduiCenterModel> {
  @override
  SduiCenterModel json2Model(Map<String, dynamic> json) => SduiCenterModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiCenterModel model) {
    return Center(
      child: SduiRender.renderFromJson(context, model.child!),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiCenterModel extends SduiModel {
  final Map<String, dynamic> child;

  SduiCenterModel({
    required this.child,
    super.id,
  });

  factory SduiCenterModel.fromJson(Map<String, dynamic> json) => _$SduiCenterModelFromJson(json);
}
