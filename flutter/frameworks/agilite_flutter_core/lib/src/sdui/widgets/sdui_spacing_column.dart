import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../sdui_context.dart';
import 'widgets.dart';

part 'sdui_spacing_column.g.dart';

class SduiSpacingColumn extends SduiWidget<SduiSpacingColumnModel> {
  @override
  SduiSpacingColumnModel json2Model(Map<String, dynamic> json) => SduiSpacingColumnModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiSpacingColumnModel model) {
    return ASpacingColumn(
      key: buildKey(model.id),
      spacing: model.spacing ?? 8,
      children: model.children
          .map(
            (json) => SduiRender.renderFromJson(context, sduiContext, json),
          )
          .toList(),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiSpacingColumnModel extends SduiModel {
  final List<Map<String, dynamic>> children;
  final double? spacing;

  const SduiSpacingColumnModel({
    this.children = const [],
    this.spacing,
    String? id,
  }) : super(id: id);

  factory SduiSpacingColumnModel.fromJson(Map<String, dynamic> json) => _$SduiSpacingColumnModelFromJson(json);
}
