// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'a_width.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

AWidth _$AWidthFromJson(Map<String, dynamic> json) => AWidth(
      type: $enumDecode(_$AWidthTypeEnumMap, json['type']),
      width: (json['width'] as num).toDouble(),
    );

Map<String, dynamic> _$AWidthToJson(AWidth instance) => <String, dynamic>{
      'type': _$AWidthTypeEnumMap[instance.type]!,
      'width': instance.width,
    };

const _$AWidthTypeEnumMap = {
  AWidthType.fixed: 'fixed',
  AWidthType.flex: 'flex',
  AWidthType.byCharCount: 'byCharCount',
};
