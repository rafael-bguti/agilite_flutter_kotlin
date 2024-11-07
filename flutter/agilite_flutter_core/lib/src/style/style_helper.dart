import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

Color? _backgroundColor;
Color? _onBackgroundColor;
Color? _primaryColor;
Color? _onPrimaryColor;
Color? _warningColor;
Color? _onWarningColor;
Color? _errorColor;
Color? _errorContainerColor;
Color? _onErrorColor;
Color? _successColor;
Color? _onSuccessColor;

Color get backgroundColor => _backgroundColor ??= colorSchema?.surface ?? Colors.red;
Color get onBackgroundColor => _onBackgroundColor ??= colorSchema?.onSurface ?? Colors.red;
Color get barrierColor => onBackgroundColor.withOpacity(0.5);
Color get primaryColor => _primaryColor ??= colorSchema?.primary ?? Colors.red;
Color get onPrimaryColor => _onPrimaryColor ??= colorSchema?.onPrimary ?? Colors.red;
Color get warningColor => _warningColor ??= coreStyleColors?.warningColor ?? Colors.red;
Color get onWarningColor => _onWarningColor ??= coreStyleColors?.onWarningColor ?? Colors.red;
Color get errorColor => _errorColor ??= colorSchema?.error ?? Colors.red;
Color get errorContainerColor => _errorContainerColor ??= colorSchema?.errorContainer ?? Colors.red;
Color get onErrorColor => _onErrorColor ??= colorSchema?.onErrorContainer ?? Colors.red;
Color get successColor => _successColor ??= coreStyleColors?.successColor ?? Colors.red;
Color get onSuccessColor => _onSuccessColor ??= coreStyleColors?.onSuccessColor ?? Colors.red;

TextTheme? get textTheme {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).textTheme;
}

ColorScheme? get colorSchema {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).colorScheme;
}

CoreStyleColors? get coreStyleColors {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).extension<CoreStyleColors>()!;
}

Widget Function(LogoDestination destination) buildLogo = (destination) {
  return Image(image: AssetImage('assets/images/logow.png'), height: destination == LogoDestination.appBar ? 18 : 40);
};

enum LogoDestination { appBar, drawer }

class StyleHelper {
  static void onChangeTheme() {
    _backgroundColor = null;
    _onBackgroundColor = null;
    _primaryColor = null;
    _onPrimaryColor = null;
    _warningColor = null;
    _onWarningColor = null;
    _errorColor = null;
    _errorContainerColor = null;
    _onErrorColor = null;
    _successColor = null;
    _onSuccessColor = null;
  }
}
