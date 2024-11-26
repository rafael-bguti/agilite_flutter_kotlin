// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'menu_item.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MenuItem _$MenuItemFromJson(Map<String, dynamic> json) => MenuItem(
      json['id'],
      type: $enumDecode(_$MenuItemTypeEnumMap, json['type']),
      title: json['title'] as String?,
      route: json['route'] as String?,
      children: (json['children'] as List<dynamic>?)
          ?.map((e) => MenuItem.fromJson(e as Map<String, dynamic>))
          .toList(),
      iconCode: (json['iconCode'] as num?)?.toInt(),
    )..parentId = json['parentId'] as String?;

Map<String, dynamic> _$MenuItemToJson(MenuItem instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'route': instance.route,
      'type': _$MenuItemTypeEnumMap[instance.type]!,
      'iconCode': instance.iconCode,
      'children': instance.children,
      'parentId': instance.parentId,
    };

const _$MenuItemTypeEnumMap = {
  MenuItemType.header: 'header',
  MenuItemType.group: 'group',
  MenuItemType.item: 'item',
  MenuItemType.divider: 'divider',
};
