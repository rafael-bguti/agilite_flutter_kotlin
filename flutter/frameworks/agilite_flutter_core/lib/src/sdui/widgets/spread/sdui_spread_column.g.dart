// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_spread_column.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiSpreadColumnModel _$SduiSpreadColumnModelFromJson(
        Map<String, dynamic> json) =>
    SduiSpreadColumnModel(
      name: json['name'] as String?,
      label: json['label'] as String?,
      type: $enumDecodeNullable(_$FieldMetadataTypeEnumMap, json['type']),
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
