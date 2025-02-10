// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_text_field.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiTextFieldModel _$SduiTextFieldModelFromJson(Map<String, dynamic> json) =>
    SduiTextFieldModel(
      type: $enumDecode(_$FieldTypeEnumMap, json['type']),
      name: json['name'] as String,
      labelText: json['labelText'] as String?,
      hintText: json['hintText'] as String?,
      helperText: json['helperText'] as String?,
      enabled: json['enabled'] as bool?,
      initialValue: json['initialValue'] as String?,
      id: json['id'] as String?,
    );

const _$FieldTypeEnumMap = {
  FieldType.string: 'string',
  FieldType.date: 'date',
  FieldType.int: 'int',
  FieldType.double: 'double',
};
