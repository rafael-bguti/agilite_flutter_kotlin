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

// //TODO - MVP - Implementar Crashlytics / S3 / Bugsnag
void _configErrorLog() {
  FlutterError.onError = (errorDetails) {
    debugPrint('Virgiii - FlutterError.onError');
    debugPrint('Error on [FlutterError.onError]: $errorDetails');

    runOnNextBuild(() {
      AError.defaultCatch(errorDetails.exception, errorDetails.stack ?? StackTrace.current);
    });
  };

  PlatformDispatcher.instance.onError = (error, stack) {
    debugPrint('Virgiii - PlatformDispatcher.instance.onError');
    debugPrint('Error on [FlutterError.onError]: $error');
    debugPrint('Stack on [FlutterError.onError]: $stack');

    runOnNextBuild(() {
      AError.defaultCatch(error, stack);
    });
    return true;
  };
}
