import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'src/routes.dart';

void main() async {
  activeProfile = CoreProfile(
    type: CoreProfileType.prod,
    apiBaseUrl: "http://localhost:8090/api",
  );

  await Boot.initialize();

  runApp(BootApp(
    appTitle: 'Agilite',
    routes: routes,
    themeMode: ThemeMode.light,
  ));
}
