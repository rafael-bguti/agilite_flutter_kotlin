import 'dart:ui';

import 'package:agilite_flutter_boot/src/events/user_exit_events_listener.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/widgets.dart';

class Boot {
  static Future<void> initialize() async {
    WidgetsFlutterBinding.ensureInitialized();
    UserExitEventsListener.registerSystemEventsListeners();

    _configErrorLog();
  }
}

void _configErrorLog() {
  FlutterError.onError = (details) {
    debugPrint('Virgiii - FlutterError.onError');
    FlutterError.presentError(details);
    // TODO - MVP - Implementar Crashlytics / S3 / Bugsnag
  };

  // TODO - Criar um componente de erro customizado e habilitar ele aqui quando estiver em produção
  //ErrorWidget.builder = (errorDetails) => const Text('ERROR!!!');

  PlatformDispatcher.instance.onError = (error, stack) {
    // //TODO - MVP - Implementar Crashlytics / S3 / Bugsnag
    debugPrint('Virgiii - PlatformDispatcher.instance.onError');
    debugPrint('Error on [FlutterError.onError]: $error');
    debugPrint('Stack on [FlutterError.onError]: $stack');

    runOnNextBuild(() {
      AError.defaultCatch(error, stack);
    });
    return true;
  };
}
