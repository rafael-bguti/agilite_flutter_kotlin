// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'field_metadata.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FieldMetadata _$FieldMetadataFromJson(Map<String, dynamic> json) =>
    FieldMetadata(
      name: json['name'] as String,
      label: json['label'] as String,
      type: $enumDecode(_$FieldMetadataTypeEnumMap, json['type']),
      req: json['req'] as bool? ?? false,
      size: (json['size'] as num?)?.toDouble(),
      options: (json['options'] as List<dynamic>?)
          ?.map((e) => FieldMetadataOption.fromJson(e as Map<String, dynamic>))
          .toList(),
      validationQuery: json['validationQuery'] as String?,
      hintText: json['hintText'] as String?,
      helperText: json['helperText'] as String?,
      autocompleteColumnId: json['autocompleteColumnId'] as String?,
      autocompleteColumnsView: json['autocompleteColumnsView'] as String?,
    );

Map<String, dynamic> _$FieldMetadataToJson(FieldMetadata instance) =>
    <String, dynamic>{
      'name': instance.name,
      'label': instance.label,
      'type': _$FieldMetadataTypeEnumMap[instance.type]!,
      'req': instance.req,
      'hintText': instance.hintText,
      'helperText': instance.helperText,
      'size': instance.size,
      'validationQuery': instance.validationQuery,
      'options': instance.options,
      'autocompleteColumnId': instance.autocompleteColumnId,
      'autocompleteColumnsView': instance.autocompleteColumnsView,
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
