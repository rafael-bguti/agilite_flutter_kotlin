import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';
import 'tables/sdui_column_model.dart';
import 'tables/sdui_spread_column.dart';

part 'sdui_crud.g.dart';

class SduiCrud extends SduiWidget<SduiCrudModel> {
  @override
  SduiCrudModel json2Model(Map<String, dynamic> json) => SduiCrudModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiContext sduiContext, SduiCrudModel model) {
    final crudDataColumns = model.columns
        .map(
          (column) => SduiSpreadColumn.build(context, sduiContext, column),
        )
        .toList();

    final customFiltersWidgets = model.customFilters
        ?.map(
          (json) => SduiRender.renderFromJson(context, sduiContext, json),
        )
        .toList();

    final formBody = model.formBody == null ? null : SduiRender.renderFromJson(context, sduiContext, model.formBody!);

    return ACrud.name(
      taskName: model.taskName,
      descr: model.descr,
      columns: crudDataColumns,
      customFilters: customFiltersWidgets,
      formBody: formBody,
      metadataToLoad: model.metadataToLoad,
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiCrudModel extends SduiModel {
  final String taskName;
  final CrudDescr descr;
  final String? metadataToLoad;
  final List<SduiColumnModel> columns;
  final List<Map<String, dynamic>>? customFilters;
  final Map<String, dynamic>? formBody;

  SduiCrudModel({
    super.id,
    required this.taskName,
    required this.descr,
    required this.columns,
    this.metadataToLoad,
    this.customFilters,
    this.formBody,
  });

  factory SduiCrudModel.fromJson(Map<String, dynamic> json) => _$SduiCrudModelFromJson(json);
}
