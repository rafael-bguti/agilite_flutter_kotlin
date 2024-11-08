import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const seedColor = Color(0xFF1E009E);

ThemeData buildLightTheme([ThemeData? theme, CoreStyleColors? coreThemeColors]) {
  return buildTheme(theme ?? _defaultBootLightTheme, coreThemeColors ?? _defaultLightCoreTheme);
}

ThemeData buildDarkTheme([ThemeData? theme, CoreStyleColors? coreThemeColors]) {
  return buildTheme(theme ?? _defaultBootDarkTheme, coreThemeColors ?? _defaultDarkCoreTheme);
}

ThemeData buildTheme(ThemeData theme, CoreStyleColors coreThemeColors) {
  return theme.copyWith(
    extensions: <ThemeExtension<dynamic>>[
      coreThemeColors,
    ],
  );
}

final ThemeData _defaultBootLightTheme = _buildThemeData(_lightColorScheme);
const _defaultLightCoreTheme = CoreStyleColors(
  name: 'AgiliteLight',
  successColor: Color(0xFF1E009E),
  onSuccessColor: Colors.white,
  appBarColor: Color(0xFFF5F7FA),
  onAppBarColor: Color(0xFF272C33),
  sideBarColor: Color(0xFF303840),
  onSideBarColor: Colors.white,
  onWarningColor: Color(0xFFE68200),
);

final ThemeData _defaultBootDarkTheme = _buildThemeData(_darkColorScheme);
const _defaultDarkCoreTheme = CoreStyleColors(
  name: 'AgiliteDark',
  successColor: Colors.pink, //Color(0xFF3404FF),
  onSuccessColor: Colors.white,
  appBarColor: Color(0xFF1D2126),
  onAppBarColor: Colors.white,
  sideBarColor: Color(0xFF303840),
  onSideBarColor: Colors.white,
);

const _buttonBorder = RoundedRectangleBorder(
  borderRadius: BorderRadius.all(Radius.circular(8)),
);

ThemeData _buildThemeData(ColorScheme colorScheme) {
  return ThemeData(
    useMaterial3: true,
    brightness: colorScheme.brightness,
    colorScheme: colorScheme,
    inputDecorationTheme: const InputDecorationTheme(border: OutlineInputBorder(), isDense: true),
    elevatedButtonTheme: ElevatedButtonThemeData(style: ElevatedButton.styleFrom(shape: _buttonBorder)),
    filledButtonTheme: FilledButtonThemeData(style: FilledButton.styleFrom(shape: _buttonBorder)),
    outlinedButtonTheme: OutlinedButtonThemeData(style: OutlinedButton.styleFrom(shape: _buttonBorder)),
    textButtonTheme: TextButtonThemeData(style: TextButton.styleFrom(shape: _buttonBorder)),
    popupMenuTheme: PopupMenuThemeData(surfaceTintColor: colorScheme.surface, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8))),
  );
}

//Schemes
final _lightColorScheme = ColorScheme.fromSeed(
  seedColor: seedColor,
  brightness: Brightness.light,
  error: const Color(0xFF981111),
  surface: const Color(0xFFF5F7FA),
  onSurface: const Color(0xFF272C33),
).copyWith();

//Schemes
final _darkColorScheme = ColorScheme.fromSeed(
  seedColor: seedColor,
  brightness: Brightness.dark,
  error: const Color(0xFFEC0000),
  errorContainer: _lightColorScheme.errorContainer,
  onErrorContainer: _lightColorScheme.onErrorContainer,
  surface: const Color(0xFF1D2126),
  onSurface: const Color(0xFFF2F9FF),
).copyWith();
