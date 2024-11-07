import 'package:flutter/material.dart';
import 'package:json_annotation/json_annotation.dart';

part 'menu_item.g.dart';

@JsonSerializable()
class MenuItem {
  final dynamic id;
  final String? title;
  final String? route;
  final MenuItemType type;
  final int? iconCode;
  final List<MenuItem>? children;

  String? parentId;

  MenuItem.header({
    required this.title,
    this.iconCode,
  })  : type = MenuItemType.header,
        route = null,
        children = null,
        id = null;

  MenuItem.group(
    this.id, {
    required this.title,
    required List<MenuItem> this.children,
    this.iconCode,
  })  : type = MenuItemType.group,
        route = null {
    _configureParentId();
  }

  MenuItem.item({
    required this.title,
    this.route,
    this.iconCode,
  })  : id = route,
        type = MenuItemType.item,
        children = null;

  MenuItem.divider()
      : id = 'divider',
        title = null,
        type = MenuItemType.divider,
        route = null,
        children = null,
        iconCode = null;

  MenuItem(
    this.id, {
    required this.type,
    this.title,
    this.route,
    this.children,
    this.iconCode,
  }) {
    _configureParentId();
  }

  void _configureParentId() {
    if (children != null) {
      for (final child in children!) {
        child.parentId = id;
        child._configureParentId();
      }
    }
  }

  IconData? get icon => iconCode == null ? null : IconData(iconCode!, fontFamily: 'MaterialIcons');

  factory MenuItem.fromJson(Map<String, dynamic> json) => _$MenuItemFromJson(json);
  Map<String, dynamic> toJson() => _$MenuItemToJson(this);
}

enum MenuType { header, item, divider }

enum MenuItemType {
  header,
  group,
  item,
  divider,
}
