import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_spacing_column.g.dart';

class SduiSpacingColumn extends SduiWidget<SduiSpacingColumnModel> {
  @override
  SduiSpacingColumnModel json2Model(Map<String, dynamic> json) => SduiSpacingColumnModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiSpacingColumnModel model) {
    return ASpacingColumn(
      key: buildKey(model.id),
      spacing: model.spacing ?? 8,
      crossAxisAlignment: model.crossAxisAlignment ?? CrossAxisAlignment.start,
      children: model.children
          .map(
            (json) => SduiRender.renderFromJson(context, json),
          )
          .toList(),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiSpacingColumnModel extends SduiModel {
  final List<Map<String, dynamic>> children;
  final double? spacing;
  final CrossAxisAlignment? crossAxisAlignment;

  const SduiSpacingColumnModel({
    this.children = const [],
    this.spacing,
    this.crossAxisAlignment,
    String? id,
  }) : super(id: id);

  factory SduiSpacingColumnModel.fromJson(Map<String, dynamic> json) => _$SduiSpacingColumnModelFromJson(json);
}
