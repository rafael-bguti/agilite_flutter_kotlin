import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

final menuItens = <MenuItem>[
  MenuItem('s', title: 'Overview', type: MenuItemType.header),
  MenuItem('a', title: 'Dashboard', type: MenuItemType.item, iconCode: 0xe1b1, route: '/dashboard'),
  MenuItem('b', title: 'Login', type: MenuItemType.item, iconCode: 0xf041),
  MenuItem('s', title: 'Applications', type: MenuItemType.header),
  MenuItem('x', title: 'Cadastros', type: MenuItemType.group, iconCode: Icons.add_chart_sharp.codePoint, children: [
    MenuItem('c', title: 'Clientes', type: MenuItemType.item, route: '/clientes'),
    MenuItem('d', title: 'Fornecedores', type: MenuItemType.item, route: '/forncedores'),
  ]),
  MenuItem('y', title: 'Processos', type: MenuItemType.group, iconCode: 0xf52f, children: [
    MenuItem('c1', title: 'Clientes', type: MenuItemType.item, route: '/forncedoresa'),
    MenuItem('d1', title: 'Dashboard', type: MenuItemType.item),
  ]),
];
