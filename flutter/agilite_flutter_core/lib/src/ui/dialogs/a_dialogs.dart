import 'package:agilite_flutter_core/core.dart';

void showLoading(String loadingMessage) {
  ALoading.show(loadingMessage);
}

void hideLoading() {
  ANavigator.closeLoadingDialog();
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

void closeError() {
  ANavigator.closeErrorDialog();
}

void closeAllDialogs() {
  ANavigator.closeAllDialogs();
}
