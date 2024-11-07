import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

ValueNotifier<String> _$loadingMessage = ValueNotifier('');

class ALoading {
  static void show(String loadingMessage) {
    _$loadingMessage.value = loadingMessage;
    if (!ANavigator.hasRouteByName(loadingRouteName)) {
      showDialog(
        routeSettings: const RouteSettings(name: loadingRouteName),
        context: globalNavigatorKey.currentContext!,
        barrierDismissible: false,
        builder: (context) {
          return Dialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12.0),
            ),
            child: const Padding(
              padding: EdgeInsets.all(32.0),
              child: _ALoadingView(),
            ),
          );
        },
      );
    }
  }

  static void hide() {
    ANavigator.closeLoadingDialog();
  }
}

class _ALoadingView extends StatelessWidget {
  const _ALoadingView({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final textTheme = theme.textTheme;

    return SingleChildScrollView(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          coreStyle.loadingWidget,
          const SizedBox(height: 22),
          Text(
            'Por favor, aguarde...',
            style: textTheme.headlineMedium,
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 8),
          SizedBox(
            width: 400,
            child: Center(
              child: ValueListenableBuilder<String>(
                valueListenable: _$loadingMessage,
                builder: (context, msg, _) {
                  return Text(
                    msg,
                    style: textTheme.titleLarge,
                    textAlign: TextAlign.center,
                  );
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
