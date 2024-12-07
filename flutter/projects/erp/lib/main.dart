import 'package:agilite_flutter_boot/boot.dart';
import 'package:flutter/material.dart';

import 'src/routes.dart';

void main() async {
  await Boot.initialize();

  runApp(BootApp(
    appTitle: 'Agilite',
    routes: routes,
    themeMode: ThemeMode.light,
  ));
}
