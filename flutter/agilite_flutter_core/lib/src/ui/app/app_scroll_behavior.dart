import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class AppScrollBehavior extends ScrollBehavior {
  @override
  // Add mouse drag on desktop for easier responsive testing
  Set<PointerDeviceKind> get dragDevices {
    final devices = Set<PointerDeviceKind>.from(super.dragDevices);
    devices.add(PointerDeviceKind.mouse);
    return devices;
  }

  // Use bouncing physics on all platforms, better matches the design of the app
  @override
  ScrollPhysics getScrollPhysics(BuildContext context) => const BouncingScrollPhysics();

  @override
  Widget buildScrollbar(BuildContext context, Widget child, ScrollableDetails details) {
    if (PlatformInfo.isMobile) return child;

    bool isScrollView = details.controller?.debugLabel == 'AScrollVisible';
    bool showScroll = !isScrollView && PlatformInfo.hasMouse;
    return RawScrollbar(
      controller: details.controller,
      thumbVisibility: showScroll,
      trackVisibility: showScroll,
      thumbColor: showScroll ? onBackgroundColor.withOpacity(0.5) : null,
      thickness: showScroll ? 8 : 0,
      interactive: showScroll ? true : false,
      child: child,
    );
  }
}
