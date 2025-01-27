import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/sdui/widgets/spread/sdui_spread_column.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../sdui_context.dart';
import '../widgets/widgets.dart';

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

    return _SduiCrudWidget(
      model: model,
      dataColumns: crudDataColumns,
      key: buildKey(model.id),
      customFilters: customFiltersWidgets,
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiCrudModel extends SduiModel {
  final String taskName;
  final CrudDescr descr;
  final List<SduiSpreadColumnModel> columns;
  final List<Map<String, dynamic>>? customFilters;

  SduiCrudModel({
    super.id,
    required this.taskName,
    required this.descr,
    required this.columns,
    this.customFilters,
  });

  factory SduiCrudModel.fromJson(Map<String, dynamic> json) => _$SduiCrudModelFromJson(json);
}

class _SduiCrudWidget extends StatelessWidget {
  final SduiCrudModel model;
  final List<ASpreadColumn> dataColumns;
  final List<Widget>? customFilters;

  _SduiCrudWidget({
    required this.model,
    required this.dataColumns,
    required super.key,
    this.customFilters,
  });

  @override
  Widget build(BuildContext context) {
    return ACrud.name(
      taskName: model.taskName,
      descr: model.descr,
      columns: dataColumns,
      customFilters: customFilters,
    );
  }
}
