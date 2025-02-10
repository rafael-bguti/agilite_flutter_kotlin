// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_grid.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiGridModel _$SduiGridModelFromJson(Map<String, dynamic> json) =>
    SduiGridModel(
      rows: (json['rows'] as List<dynamic>)
          .map((e) => SduiGridRow.fromJson(e as Map<String, dynamic>))
          .toList(),
      spacing: (json['spacing'] as num?)?.toDouble(),
      crossAxisAlignment: $enumDecodeNullable(
          _$WrapCrossAlignmentEnumMap, json['crossAxisAlignment']),
      id: json['id'] as String?,
    );

const _$WrapCrossAlignmentEnumMap = {
  WrapCrossAlignment.start: 'start',
  WrapCrossAlignment.end: 'end',
  WrapCrossAlignment.center: 'center',
};

SduiGridRow _$SduiGridRowFromJson(Map<String, dynamic> json) => SduiGridRow(
      areas: json['areas'] as String,
      children: (json['children'] as List<dynamic>)
          .map((e) => e as Map<String, dynamic>)
          .toList(),
    );
