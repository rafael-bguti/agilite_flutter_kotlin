import 'dart:io';

import 'package:flutter/foundation.dart' show kIsWeb;

class PlatformInfo {
  static bool get isMobile {
    return !isWeb && (Platform.isAndroid || Platform.isIOS);
  }

  static bool get isDesktop {
    return !isWeb && (Platform.isWindows || Platform.isLinux || Platform.isMacOS);
  }

  static bool get isWeb {
    return kIsWeb;
  }

  static bool get hasMouse {
    return isDesktop || isWeb;
  }
}
