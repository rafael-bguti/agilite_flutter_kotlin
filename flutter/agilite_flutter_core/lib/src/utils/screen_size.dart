import 'package:flutter/material.dart';

const double _phoneMaxBreakpoint = 850;
const double _tabletMaxBreakpoint = 1100;

enum Device { phone, tablet, desktop }

class ScreenSize {
  final MediaQueryData mq;

  ScreenSize(BuildContext context) : mq = MediaQuery.of(context);

  bool isPhone() {
    return isPhoneSize(mq.size.width);
  }

  bool isPhoneSize(double size) {
    return size <= _phoneMaxBreakpoint;
  }

  bool isTablet() {
    return isTabletSize(mq.size.width);
  }

  bool isTabletSize(double size) {
    return size > _phoneMaxBreakpoint && size <= _tabletMaxBreakpoint;
  }

  bool isDesktop() {
    return isDesktopSize(mq.size.width);
  }

  bool isDesktopSize(double size) {
    return size > _tabletMaxBreakpoint;
  }

  double maxWidth(BoxConstraints constraints) {
    return constraints.hasInfiniteWidth ? mq.size.width : constraints.maxWidth;
  }

  Device whichDevice() {
    final width = mq.size.width;
    if (isDesktopSize(width)) {
      return Device.desktop;
    } else if (isTabletSize(width)) {
      return Device.tablet;
    } else {
      return Device.phone;
    }
  }

  double horizontalPadding() {
    if (isPhone()) {
      return 8;
    } else if (isTablet()) {
      return 16;
    } else {
      return 24;
    }
  }
}
