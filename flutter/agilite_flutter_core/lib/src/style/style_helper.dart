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

Color get backgroundColor => _backgroundColor ??= colorScheme?.surface ?? Colors.red;
Color get onBackgroundColor => _onBackgroundColor ??= colorScheme?.onSurface ?? Colors.red;
Color get primaryColor => _primaryColor ??= colorScheme?.primary ?? Colors.red;
Color get onPrimaryColor => _onPrimaryColor ??= colorScheme?.onPrimary ?? Colors.red;
Color get warningColor => _warningColor ??= coreStyleColors?.warningColor ?? Colors.red;
Color get onWarningColor => _onWarningColor ??= coreStyleColors?.onWarningColor ?? Colors.red;
Color get errorColor => _errorColor ??= colorScheme?.error ?? Colors.red;
Color get errorContainerColor => _errorContainerColor ??= colorScheme?.errorContainer ?? Colors.red;
Color get onErrorColor => _onErrorColor ??= colorScheme?.onErrorContainer ?? Colors.red;
Color get successColor => _successColor ??= coreStyleColors?.successColor ?? Colors.red;
Color get onSuccessColor => _onSuccessColor ??= coreStyleColors?.onSuccessColor ?? Colors.red;

final ValueNotifier<ThemeMode> themeNotifier = ValueNotifier(ThemeMode.system);

TextTheme? get textTheme {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).textTheme;
}

ColorScheme? get colorScheme {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).colorScheme;
}

CoreStyleColors? get coreStyleColors {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return null;
  return Theme.of(context).extension<CoreStyleColors>()!;
}

Brightness get brightness {
  final context = globalNavigatorKey.currentContext;
  if (context == null) return Brightness.light;
  return Theme.of(context).brightness;
}

//---- Instance of CoreStyle ----
CoreStyle? _coreStyle;
set coreStyle(CoreStyle coreStyle) {
  _coreStyle = coreStyle;
}

CoreStyle get coreStyle {
  return _coreStyle ??= const CoreStyle();
}
//---- Instance of CoreStyle ----

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
