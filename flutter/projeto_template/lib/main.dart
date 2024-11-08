import 'package:agilite_flutter_boot/boot.dart';
import 'package:flutter/material.dart';
import 'package:projeto_estudo/src/routes.dart';

//Implementar esse template como padrao
// https://huma.demo.frontendmatter.com/compact-ui-forms.html
// Atenção aos separadores dos forms

void main() async {
  await Boot.initialize();

  runApp(BootApp(
    appTitle: 'Agilite Flutter Boot',
    routes: routes,
    themeMode: ThemeMode.light,
  ));
}
