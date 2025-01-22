import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AError {
  static void defaultCatch(
    dynamic error,
    StackTrace stackTrace, {
    bool closeAllDialogsBefore = true,
    String? pathToReplaceOnClose,
  }) {
    defaultCatchState(
      ErrorState(error: error, stack: stackTrace),
      closeAllDialogsBefore: closeAllDialogsBefore,
      pathToReplaceOnClose: pathToReplaceOnClose,
    );
  }

  static void defaultCatchState(
    ErrorState errorState, {
    bool closeAllDialogsBefore = true,
    String? pathToReplaceOnClose,
  }) {
    if (errorState.error is ValidationException) {
      showWarning(
        errorState.error.toString(),
        closeAllDialogsBefore: closeAllDialogsBefore,
        pathToReplaceOnClose: pathToReplaceOnClose,
      );
    } else {
      defaultCatchView(
        AErrorView(state: errorState, pathToReplaceOnClose: pathToReplaceOnClose),
        closeAllDialogsBefore: closeAllDialogsBefore,
      );
    }
  }

  static void defaultCatchView(
    AErrorView errorView, {
    bool closeAllDialogsBefore = true,
  }) {
    if (closeAllDialogsBefore) {
      ANavigator.closeAllDialogs();
    }
    final backgroundColor = errorView.backgroundColor ?? colorScheme!.errorContainer;

    showDialog(
      routeSettings: const RouteSettings(name: errorRouteName),
      context: globalNavigatorKey.currentContext!,
      barrierDismissible: false,
      builder: (context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.0),
          ),
          backgroundColor: backgroundColor,
          child: errorView,
        );
      },
    );
  }

  static void close() {
    ANavigator.closeErrorDialog();
  }
}

class AErrorView extends StatelessWidget {
  final ErrorState state;
  final String? imageAssetPath;

  final Color? foregroundColor;
  final Color? backgroundColor;

  final String? pathToReplaceOnClose;

  const AErrorView({
    required this.state,
    this.imageAssetPath,
    this.foregroundColor,
    this.backgroundColor,
    this.pathToReplaceOnClose,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: const BoxConstraints(
        maxHeight: 600,
      ),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: IntrinsicHeight(
          child: Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Image(image: AssetImage(_imagePath), height: 100),
                      const SizedBox(height: 16),
                      if (_title != null)
                        SelectableText(
                          _title!,
                          style: textTheme!.headlineSmall!.copyWith(color: _foregroundColor),
                          textAlign: TextAlign.center,
                        ),
                      const SizedBox(height: 8),
                      SelectableText(
                        _subTitle,
                        style: textTheme!.bodyLarge!.copyWith(color: _foregroundColor),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 8),
                      if (_stack != null)
                        SelectableText(
                          _stack!,
                          style: textTheme!.bodySmall!.copyWith(color: _foregroundColor),
                          textAlign: TextAlign.left,
                        ),
                      const SizedBox(height: 20),
                    ],
                  ),
                ),
              ),
              FilledButton.icon(
                onPressed: _onCloseButtonPressed,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.white,
                  foregroundColor: Colors.black,
                  padding: const EdgeInsets.symmetric(horizontal: 32),
                ),
                icon: const Icon(Icons.close),
                label: const Text('Fechar'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _onCloseButtonPressed() {
    if (state.onBackButtonPressed != null) {
      state.onBackButtonPressed!();
    } else {
      if (pathToReplaceOnClose != null) {
        ANavigator.replace(pathToReplaceOnClose!);
      } else {
        if (state.error is UnauthenticatedException || state.error is ForbiddenException || state.error is ForceExitException) {
          coreEventBus.fire(SysEventOnExitButtonTap(askBeforeLeaving: false));
        } else if (state.error is ForceHomeException) {
          ANavigator.replace(dashboardPath);
        } else {
          ANavigator.closeErrorDialog();
        }
      }
    }
  }

  Color get _foregroundColor {
    if (foregroundColor != null) {
      return foregroundColor!;
    } else {
      return errorColor;
    }
  }

  String get _imagePath {
    return 'packages/agilite_flutter_core/assets/error.png';
  }

  String? get _title {
    return state.title ?? 'Erro';
  }

  String get _subTitle {
    return state.error.toString();
  }

  String? get _stack {
    return state.stack.toString();
  }
}
