import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../sdui_context.dart';
import 'widgets.dart';

part 'sdui_column.g.dart';

class SduiColumn extends SduiWidget<SduiColumnModel> {
  @override
  SduiColumnModel json2Model(Map<String, dynamic> json) => SduiColumnModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiColumnModel model) {
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

@JsonSerializable()
class SduiColumnModel extends SduiModel {
  final List<Map<String, dynamic>> children;
  final double? spacing;

  const SduiColumnModel({
    this.children = const [],
    this.spacing,
    String? id,
  }) : super(id: id);

  factory SduiColumnModel.fromJson(Map<String, dynamic> json) => _$SduiColumnModelFromJson(json);
}
