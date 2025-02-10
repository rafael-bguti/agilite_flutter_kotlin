import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

import 'sdui_widget.dart';

part 'sdui_date_range.g.dart';

class SduiDateRange extends SduiWidget<SduiDateRangeModel> {
  @override
  SduiDateRangeModel json2Model(Map<String, dynamic> json) => SduiDateRangeModel.fromJson(json);

  @override
  Widget render(BuildContext context, SduiDateRangeModel model) {
    return ADateRange(
      nameIni: model.nameIni,
      nameEnd: model.nameFim,
      fluid: model.fluid,
      label: model.label,
    );
  }
}

@JsonSerializable(createToJson: false)
class SduiDateRangeModel extends SduiModel {
  final String? label;
  final String nameIni;
  final String nameFim;
  final bool fluid;

  SduiDateRangeModel({
    required this.nameIni,
    required this.nameFim,
    this.label,
    this.fluid = false,
    super.id,
  });

  factory SduiDateRangeModel.fromJson(Map<String, dynamic> json) => _$SduiDateRangeModelFromJson(json);
}
