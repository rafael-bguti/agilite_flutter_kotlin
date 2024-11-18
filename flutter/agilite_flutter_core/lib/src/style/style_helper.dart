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
Color? _onSurfaceColor;

Color get backgroundColor => _backgroundColor ??= colorScheme?.surface ?? Colors.red;
Color get onBackgroundColor => _onBackgroundColor ??= colorScheme?.onSurface ?? Colors.red;
Color get barrierColor => onBackgroundColor.withOpacity(0.5);
Color get primaryColor => _primaryColor ??= colorScheme?.primary ?? Colors.red;
Color get onPrimaryColor => _onPrimaryColor ??= colorScheme?.onPrimary ?? Colors.red;
Color get warningColor => _warningColor ??= coreStyleColors?.warningColor ?? Colors.red;
Color get onWarningColor => _onWarningColor ??= coreStyleColors?.onWarningColor ?? Colors.red;
Color get errorColor => _errorColor ??= colorScheme?.error ?? Colors.red;
Color get errorContainerColor => _errorContainerColor ??= colorScheme?.errorContainer ?? Colors.red;
Color get onErrorColor => _onErrorColor ??= colorScheme?.onError ?? Colors.red;
Color get successColor => _successColor ??= coreStyleColors?.successColor ?? Colors.red;
Color get onSuccessColor => _onSuccessColor ??= coreStyleColors?.onSuccessColor ?? Colors.red;
Color get onSurfaceColor => _onSurfaceColor ??= colorScheme?.onSurface ?? Colors.red;

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

//-------- TextStyles --------
final moreDetailTextStyle = TextStyle(color: onBackgroundColor.lightenOrDarken(brightness, 0.3), fontSize: 13);

//-------- Buttons --------
ButtonStyle successButtonStyle = buildButtonStyle(successColor, onSuccessColor);
ButtonStyle warningButtonStyle = buildButtonStyle(warningColor, onWarningColor);

ButtonStyle buildButtonStyle(Color backgroundColor, Color foregroundColor) {
  return ButtonStyle(
    backgroundColor: WidgetStateProperty.resolveWith<Color?>(
      (Set<WidgetState> states) {
        if (states.contains(WidgetState.disabled)) return null;
        return backgroundColor;
      },
    ),
    foregroundColor: WidgetStateProperty.resolveWith<Color?>(
      (Set<WidgetState> states) {
        if (states.contains(WidgetState.disabled)) return null;
        return foregroundColor;
      },
    ),
  );
}

ButtonStyle buildOutlinedButtonStyle(Color color) {
  return OutlinedButton.styleFrom(
    side: BorderSide(color: color), // Cor da borda
    foregroundColor: color, // Cor do texto
  );
}

ButtonStyle buildTextButtonStyle(Color color) {
  return OutlinedButton.styleFrom(
    foregroundColor: color, // Cor do texto
  );
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
    _onSurfaceColor = null;
  }
}
