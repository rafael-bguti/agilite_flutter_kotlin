import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/sdui/widgets/sdui_widget.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_fieldset.g.dart';

class SduiFieldset extends SduiWidget<SduiFieldsetModel> {
  @override
  SduiFieldsetModel json2Model(Map<String, dynamic> json) => SduiFieldsetModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiFieldsetModel model) {
    return AFieldset(
      key: buildKey(model.id),
      title: model.title,
      child: SduiRender.renderFromJson(context, sduiContext, model.child),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiFieldsetModel extends SduiModel {
  final Map<String, dynamic> child;
  final String? title;

  SduiFieldsetModel({
    required this.child,
    this.title,
    super.id,
  });

  factory SduiFieldsetModel.fromJson(Map<String, dynamic> json) => _$SduiFieldsetModelFromJson(json);
}
