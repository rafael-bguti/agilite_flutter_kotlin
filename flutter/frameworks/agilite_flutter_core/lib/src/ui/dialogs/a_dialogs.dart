import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

void showLoading(String loadingMessage) {
  ALoading.show(loadingMessage);
}

void hideLoading() {
  ANavigator.closeLoadingDialog();
}

Future<void> showSuccess(
  String message, {
  bool closeAllDialogsBefore = true,
  String? pathToReplaceOnClose,
  void Function()? onClosePressed,
}) {
  return _showDialog(
    _DialogType.success,
    message,
    closeAllDialogsBefore: closeAllDialogsBefore,
    pathToReplaceOnClose: pathToReplaceOnClose,
    onClosePressed: onClosePressed,
  );
}

Future<void> showWarning(
  String message, {
  bool closeAllDialogsBefore = true,
  String? pathToReplaceOnClose,
  void Function()? onClosePressed,
}) {
  return _showDialog(
    _DialogType.warning,
    message,
    closeAllDialogsBefore: closeAllDialogsBefore,
    pathToReplaceOnClose: pathToReplaceOnClose,
    onClosePressed: onClosePressed,
  );
}

void showError(
  dynamic error,
  StackTrace stackTrace, {
  bool closeAllDialogsBefore = true,
  String? pathToReplaceOnClose,
}) {
  AError.defaultCatch(
    error,
    stackTrace,
    closeAllDialogsBefore: closeAllDialogsBefore,
    pathToReplaceOnClose: pathToReplaceOnClose,
  );
}

void showQuestionMessage(
  String message,
  Future<void> Function(BuildContext context) onConfirm,
) {
  showQuestion(
    ABottomDialogQuestion(
      message: message,
      onConfirm: onConfirm,
    ),
  );
}

void showQuestion(
  ABottomDialogQuestion questionDialog,
) {
  showModalBottomSheet(
    routeSettings: const RouteSettings(name: questionRouteName),
    context: globalNavigatorKey.currentContext!,
    enableDrag: true,
    showDragHandle: true,
    constraints: BoxConstraints(
      minWidth: MediaQuery.of(globalNavigatorKey.currentContext!).size.width,
    ),
    builder: (ctx) => questionDialog,
  );
}

void showErrorState(
  ErrorState errorState, {
  bool closeAllDialogsBefore = true,
  String? pathToReplaceOnClose,
}) {
  AError.defaultCatchState(
    errorState,
    closeAllDialogsBefore: closeAllDialogsBefore,
    pathToReplaceOnClose: pathToReplaceOnClose,
  );
}

void hideError() {
  ANavigator.closeErrorDialog();
}

void hideAllDialogs() {
  ANavigator.closeAllDialogs();
}

enum _DialogType { success, warning }

Future<void> _showDialog(
  _DialogType type,
  String message, {
  bool closeAllDialogsBefore = true,
  String? pathToReplaceOnClose,
  void Function()? onClosePressed,
}) {
  if (closeAllDialogsBefore) {
    ANavigator.closeAllDialogs();
  }
  final routingName = type == _DialogType.warning ? warningRouteName : successRouteName;
  final icon = type == _DialogType.warning ? Icons.warning : Icons.check_circle;
  final color = type == _DialogType.warning ? Colors.orange : successColor;
  final title = type == _DialogType.warning ? 'Oooops!!!' : 'Tudo certo!';

  return showDialog(
    routeSettings: RouteSettings(name: routingName),
    context: globalNavigatorKey.currentContext!,
    barrierDismissible: false,
    builder: (context) {
      return AlertDialog(
        icon: Icon(icon, color: color, size: 44),
        title: Text(title, style: TextStyle(color: color)),
        content: Text(message, style: const TextStyle(fontWeight: FontWeight.bold)),
        actions: [
          TextButton(
            onPressed: () {
              if (onClosePressed != null) {
                onClosePressed();
              } else {
                if (pathToReplaceOnClose != null) {
                  ANavigator.replace(pathToReplaceOnClose);
                } else {
                  globalNavigatorKey.currentState!.pop();
                }
              }
            },
            child: const Text('Fechar'),
          ),
        ],
      );
    },
  );
}
