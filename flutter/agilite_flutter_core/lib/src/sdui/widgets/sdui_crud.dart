import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/sdui/widgets/spread/sdui_spread_column.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import '../sdui_context.dart';
import 'widgets.dart';

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

    return _SduiCrudWidget(
      model: model,
      dataColumns: crudDataColumns,
      key: buildKey(model.id),
    );
  }
}

@JsonSerializable()
class SduiCrudModel extends SduiModel {
  final CrudDescr descr;
  final List<SduiSpreadColumnModel> columns;

  SduiCrudModel({
    super.id,
    required this.descr,
    required this.columns,
  });

  factory SduiCrudModel.fromJson(Map<String, dynamic> json) => _$SduiCrudModelFromJson(json);
}

class _SduiCrudWidget extends StatefulWidget {
  final SduiCrudModel model;
  final List<ASpreadColumn> dataColumns;

  _SduiCrudWidget({
    required this.model,
    required this.dataColumns,
    required super.key,
  });

  @override
  State<_SduiCrudWidget> createState() => _SduiCrudWidgetState();
}

class _SduiCrudWidgetState extends State<_SduiCrudWidget> {
  late final CrudController crudController = CrudController();

  @override
  void dispose() {
    crudController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ACrud(
      controller: crudController,
      descr: widget.model.descr,
      columns: widget.dataColumns,
    );
  }
}
