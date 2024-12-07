// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_spread_column.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiSpreadColumnModel _$SduiSpreadColumnModelFromJson(
        Map<String, dynamic> json) =>
    SduiSpreadColumnModel(
      fieldMetadataName: json['fieldMetadataName'] as String?,
      name: json['name'] as String?,
      label: json['label'] as String?,
      type: $enumDecodeNullable(_$FieldMetadataTypeEnumMap, json['type']),
      width: json['width'] == null
          ? null
          : AWidth.fromJson(json['width'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$SduiSpreadColumnModelToJson(
        SduiSpreadColumnModel instance) =>
    <String, dynamic>{
      'fieldMetadataName': instance.fieldMetadataName,
      'name': instance.name,
      'label': instance.label,
      'type': _$FieldMetadataTypeEnumMap[instance.type],
      'width': instance.width,
    };

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
