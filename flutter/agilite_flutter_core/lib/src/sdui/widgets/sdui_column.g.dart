// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_column.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiColumnModel _$SduiColumnModelFromJson(Map<String, dynamic> json) =>
    SduiColumnModel(
      children: (json['children'] as List<dynamic>?)
              ?.map((e) => e as Map<String, dynamic>)
              .toList() ??
          const [],
      spacing: (json['spacing'] as num?)?.toDouble(),
      id: json['id'] as String?,
    );

Map<String, dynamic> _$SduiColumnModelToJson(SduiColumnModel instance) =>
    <String, dynamic>{
      'id': instance.id,
      'children': instance.children,
      'spacing': instance.spacing,
    };
