import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/sdui/sdui_context.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'widgets.dart';

part 'sdui_sized_box.g.dart';

class SduiSizedBox extends SduiWidget<SduiSizedBoxModel> {
  @override
  SduiSizedBoxModel json2Model(Map<String, dynamic> json) => SduiSizedBoxModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiSizedBoxModel model) {
    return SizedBox(
      width: model.width,
      height: model.height,
      key: buildKey(model.id),
      child: SduiRender.renderFromJson(context, sduiContext, model.child),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiSizedBoxModel extends SduiModel {
  final Map<String, dynamic> child;
  final double? width;
  final double? height;

  SduiSizedBoxModel({
    required this.child,
    this.width,
    this.height,
    super.id,
  });

  factory SduiSizedBoxModel.fromJson(Map<String, dynamic> json) => _$SduiSizedBoxModelFromJson(json);
}
