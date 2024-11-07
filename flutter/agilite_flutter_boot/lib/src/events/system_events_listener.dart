import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import '../../boot.dart';

class SystemEventsListener {
  static void addSystemEventsListeners() {
    coreEventBus.on<SysEventOnExitButtonTap>().listen((event) {
      _onSysEventExitButtonClicked();
    });
  }

  static void _onSysEventExitButtonClicked() {
    showModalBottomSheet(
      context: globalNavigatorKey.currentContext!,
      enableDrag: true,
      showDragHandle: true,
      constraints: BoxConstraints(
        minWidth: MediaQuery.of(globalNavigatorKey.currentContext!).size.width,
      ),
      builder: (ctx) => ABottomDialogQuestion(
        popOnConfirm: false,
        icon: Icons.exit_to_app,
        message: "Deseja sair do sistema?",
        onConfirm: (context) => _onConfirmExitButtonClick(context),
      ),
    );
  }

  static Future<void> _onConfirmExitButtonClick(BuildContext ctx) async {
    ANavigator.go(loginPath);
    await authService.signOut();
  }
}
