import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

final menuItens = <MenuItem>[
  MenuItem('1', title: 'Overview', type: MenuItemType.header),
  MenuItem('2', title: 'Dashboard', type: MenuItemType.item, iconCode: 0xe1b1, route: '/dashboard'),
  MenuItem('9', title: 'APPLICATIONS', type: MenuItemType.header),
  MenuItem('5', title: 'Enterprise', type: MenuItemType.group, iconCode: 0xe683, children: []),
  MenuItem('3', title: 'UI', type: MenuItemType.header),
  MenuItem('4', title: 'Screens', type: MenuItemType.group, iconCode: Icons.web.codePoint, children: [
    MenuItem('41', title: 'Login', type: MenuItemType.item, route: '/login'),
  ]),
  MenuItem('6', title: 'Components', type: MenuItemType.group, iconCode: Icons.input.codePoint, children: [
    MenuItem('61', title: 'Buttons', type: MenuItemType.item, route: '/buttons'),
    MenuItem('62', title: 'Cards', type: MenuItemType.item, route: '/cards'),
    MenuItem('63', title: 'Alerts', type: MenuItemType.item, route: '/alerts'),
    MenuItem('64', title: 'Tables / Spread', type: MenuItemType.item, route: '/tables'),
    MenuItem('65', title: 'Forms', type: MenuItemType.item, route: '/forms'),
    MenuItem('69999', title: 'Diversos', type: MenuItemType.item, route: '/diversos'), //TODO criar uma pagina com todos os componentes customizados criados
  ]),
];
