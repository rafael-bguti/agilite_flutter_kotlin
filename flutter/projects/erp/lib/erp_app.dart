import 'package:agilite_flutter_boot/boot.dart';
import 'package:erp/src/routes.dart';
import 'package:flutter/material.dart';

class ERPApp extends StatelessWidget {
  const ERPApp({super.key});

  @override
  Widget build(BuildContext context) {
    return BootApp(
      appTitle: 'Agilite',
      routes: routes,
      themeMode: ThemeMode.light,
    );
  }
}
