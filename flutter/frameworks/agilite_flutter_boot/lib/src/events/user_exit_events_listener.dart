import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import '../../boot.dart';

class UserExitEventsListener {
  static void registerSystemEventsListeners() {
    coreEventBus.on<SysEventOnExitButtonTap>().listen((event) {
      if (event.askBeforeLeaving) {
        _onSysEventExitButtonClicked();
      } else {
        _onConfirmExitButtonClick();
      }
    });
  }

  static void _onSysEventExitButtonClicked() {
    showModalBottomSheet(
      routeSettings: const RouteSettings(name: questionRouteName),
      context: globalNavigatorKey.currentContext!,
      enableDrag: true,
      showDragHandle: true,
      constraints: BoxConstraints(
        minWidth: MediaQuery.of(globalNavigatorKey.currentContext!).size.width,
      ),
      builder: (ctx) => ABottomDialogQuestion(
        primary: errorColor,
        popOnConfirm: false,
        icon: Icons.exit_to_app,
        message: "Deseja sair do sistema?",
        onConfirm: (context) => _onConfirmExitButtonClick(),
      ),
    );
  }

  static Future<void> _onConfirmExitButtonClick() async {
    ANavigator.go(loginPath);
    await authService.signOut();
  }
}
