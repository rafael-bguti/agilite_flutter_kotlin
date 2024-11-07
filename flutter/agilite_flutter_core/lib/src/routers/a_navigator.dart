import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

final GlobalKey<NavigatorState> globalNavigatorKey = GlobalKey<NavigatorState>(debugLabel: 'root_navigator');

const String loadingRouteName = 'loading';
const String errorRouteName = 'error';

class ANavigator {
  static void go(String path, [bool closeAllDialogsBefore = true]) {
    if (closeAllDialogsBefore) {
      ANavigator.closeAllDialogs();
    }
    appRouter.go(path);
  }

  static void replace(String path, [bool closeAllDialogsBefore = true]) {
    if (closeAllDialogsBefore) {
      ANavigator.closeAllDialogs();
    }
    appRouter.replace(path);
  }

  static void refresh() {
    print('refresh');
    appRouter.refresh();
  }

  static void closeLoadingDialog() {
    globalNavigatorKey.currentState!.popUntil((route) => route.settings.name != loadingRouteName);
  }

  static void closeErrorDialog() {
    globalNavigatorKey.currentState!.popUntil((route) => route.settings.name != errorRouteName);
  }

  static bool hasRouteByName(String name) {
    return globalRouterObserver.routeStack.any((route) => route.settings.name == name);
  }

  static void closeAllDialogs() {
    globalNavigatorKey.currentState!.popUntil((route) {
      return route is PageRoute;
    });
  }
}
