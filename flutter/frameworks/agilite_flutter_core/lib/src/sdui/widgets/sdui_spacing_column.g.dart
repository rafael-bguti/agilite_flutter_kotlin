// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_spacing_column.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiSpacingColumnModel _$SduiSpacingColumnModelFromJson(
        Map<String, dynamic> json) =>
    SduiSpacingColumnModel(
      children: (json['children'] as List<dynamic>?)
              ?.map((e) => e as Map<String, dynamic>)
              .toList() ??
          const [],
      spacing: (json['spacing'] as num?)?.toDouble(),
      id: json['id'] as String?,
    );
