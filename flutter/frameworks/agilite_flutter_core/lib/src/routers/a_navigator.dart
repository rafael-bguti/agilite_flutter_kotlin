import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

final GlobalKey<NavigatorState> globalNavigatorKey = GlobalKey<NavigatorState>(debugLabel: 'root_navigator');

const String loadingRouteName = 'loading';
const String errorRouteName = 'error';
const String warningRouteName = 'warning';
const String successRouteName = 'success';
const String questionRouteName = 'question';

const Set<String> dialogRouteNames = {
  loadingRouteName,
  errorRouteName,
  warningRouteName,
  successRouteName,
  questionRouteName,
};

typedef NavigatorListener = void Function(String path);

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

  static Future<T?> push<T>(String path, [bool closeAllDialogsBefore = true]) {
    if (closeAllDialogsBefore) {
      ANavigator.closeAllDialogs();
    }
    return appRouter.push<T>(path);
  }

  static Future<T?> pushWidget<T>(Widget widget, [bool closeAllDialogsBefore = true]) {
    if (closeAllDialogsBefore) {
      ANavigator.closeAllDialogs();
    }

    return Navigator.of(globalNavigatorKey.currentContext!).push(MaterialPageRoute(builder: (context) => widget));
  }

  static void pop<T>([T? result]) {
    appRouter.pop(result);
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

  static void closeAllDialogs() {
    globalNavigatorKey.currentState!.popUntil((route) => !dialogRouteNames.contains(route.settings.name));
  }

  static bool isInDialog(BuildContext context) {
    return context.findAncestorWidgetOfExactType<Dialog>() != null;
  }

  static bool canPop() {
    return globalNavigatorKey.currentState!.canPop();
  }

  static bool hasRouteByName(String name) {
    return globalRouterObserver.routeStack.any((route) => route.settings.name == name);
  }
}
