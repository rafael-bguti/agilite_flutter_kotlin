import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_grid.g.dart';

class SduiGrid extends SduiWidget<SduiGridModel> {
  @override
  SduiGridModel json2Model(Map<String, dynamic> json) => SduiGridModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiGridModel model) {
    return AGrid.rows(
      rows: model.rows
          .map(
            (el) => el.toAGridRow(context),
          )
          .toList(),
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiGridModel extends SduiModel {
  final List<SduiGridRow> rows;
  final int? spacing;
  final WrapCrossAlignment? crossAxisAlignment;

  SduiGridModel({
    required this.rows,
    this.spacing = 8,
    this.crossAxisAlignment = WrapCrossAlignment.start,
    super.id,
  });

  factory SduiGridModel.fromJson(Map<String, dynamic> json) => _$SduiGridModelFromJson(json);
}

@JsonSerializable(createToJson: false)
class SduiGridRow {
  final String areas;
  final List<Map<String, dynamic>> children;

  SduiGridRow({
    required this.areas,
    required this.children,
  });

  AGridRow toAGridRow(BuildContext context) {
    return AGridRow(
      areas: areas,
      children: children
          .map(
            (json) => SduiRender.renderFromJson(context, json),
          )
          .toList(),
    );
  }

  factory SduiGridRow.fromJson(Map<String, dynamic> json) => _$SduiGridRowFromJson(json);
}
