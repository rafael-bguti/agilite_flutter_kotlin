// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_column_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiColumnModel _$SduiColumnModelFromJson(Map<String, dynamic> json) =>
    SduiColumnModel(
      name: json['name'] as String,
      type: $enumDecode(_$FieldMetadataTypeEnumMap, json['type']),
      label: json['label'] as String?,
      options: (json['options'] as List<dynamic>?)
          ?.map((e) => LocalOption<dynamic>.fromJson(e as Map<String, dynamic>))
          .toList(),
      width: json['width'] == null
          ? null
          : AWidth.fromJson(json['width'] as Map<String, dynamic>),
      mod: json['mod'] as String?,
    );

const _$FieldMetadataTypeEnumMap = {
  FieldMetadataType.string: 'string',
  FieldMetadataType.int: 'int',
  FieldMetadataType.double: 'double',
  FieldMetadataType.bool: 'bool',
  FieldMetadataType.date: 'date',
  FieldMetadataType.time: 'time',
  FieldMetadataType.timestamp: 'timestamp',
  FieldMetadataType.map: 'map',
  FieldMetadataType.autocomplete: 'autocomplete',
};
