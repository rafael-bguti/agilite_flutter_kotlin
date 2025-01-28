import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_sized_box.g.dart';

class SduiSizedBox extends SduiWidget<SduiSizedBoxModel> {
  @override
  SduiSizedBoxModel json2Model(Map<String, dynamic> json) => SduiSizedBoxModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiSizedBoxModel model) {
    if (model.width == 0 && model.height == 0) {
      return const SizedBox.shrink();
    }

    return SizedBox(
      width: model.width,
      height: model.height,
      key: buildKey(model.id),
      child: model.child == null ? null : SduiRender.renderFromJson(context, sduiContext, model.child!),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiSizedBoxModel extends SduiModel {
  final Map<String, dynamic>? child;
  final double? width;
  final double? height;

  SduiSizedBoxModel({
    this.child,
    this.width,
    this.height,
    super.id,
  });

  factory SduiSizedBoxModel.fromJson(Map<String, dynamic> json) => _$SduiSizedBoxModelFromJson(json);
}
