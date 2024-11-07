import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () {
                StyleHelper.onChangeTheme();
                if (themeNotifier.value == ThemeMode.system) {
                  final brightness = MediaQuery.of(context).platformBrightness;
                  themeNotifier.value = brightness == Brightness.dark ? ThemeMode.light : ThemeMode.dark;
                } else {
                  themeNotifier.value = themeNotifier.value == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
                }
              },
              child: Text("Troca tema"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                ANavigator.go(loginPath);
              },
              child: Text("Voltar"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: runProcess,
              child: Text("Rodar processo"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: toggleMenu1,
              child: Text("Toggle menu 1"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: toggleMenu2,
              child: Text("Toggle menu 2"),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: selectItem,
              child: Text("Select item pela rota"),
            ),
          ],
        ),
      ),
    );
  }

  void toggleMenu1() {
    fullLayoutFacade.toggleMenuGroup("x");
  }

  void toggleMenu2() {
    fullLayoutFacade.toggleMenuGroup("y");
  }

  void selectItem() {
    fullLayoutFacade.selectItemByRoute('/clientes');
  }

  void runProcess() async {
    ALoading.show("Rodando processo...");

    await Future.delayed(const Duration(milliseconds: 150));
    ALoading.show("Rodando mais um");
    await Future.delayed(const Duration(milliseconds: 200));
    // AError.defaultCatch(UnauthenticatedException(), StackTrace.current);

    ANavigator.replace(loginPath, false);
  }
}
