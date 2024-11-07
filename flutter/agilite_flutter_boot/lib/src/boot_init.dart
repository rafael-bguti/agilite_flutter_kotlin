import 'dart:ui';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/widgets.dart';

import 'events/system_events_listener.dart';

class Boot {
  static Future<void> initialize() async {
    WidgetsFlutterBinding.ensureInitialized();
    SystemEventsListener.addSystemEventsListeners();

    _configErrorLog();
  }
}

// //TODO - MVP - Implementar Crashlytics / S3 / Bugsnag
void _configErrorLog() {
  FlutterError.onError = (errorDetails) {
    debugPrint('Virgiii');
    debugPrint('Error on [FlutterError.onError]: $errorDetails');

    AError.defaultCatch(errorDetails.exception, errorDetails.stack ?? StackTrace.current);
  };

  PlatformDispatcher.instance.onError = (error, stack) {
    debugPrint('Virgiii');
    debugPrint('Error on [FlutterError.onError]: $error');
    debugPrint('Stack on [FlutterError.onError]: $stack');

    AError.defaultCatch(error, stack);
    return true;
  };
}
