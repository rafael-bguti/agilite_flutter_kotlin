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
  successColor: Colors.teal,
  onSuccessColor: Colors.white,
  appBarColor: Color(0xFFF5F7FA),
  onAppBarColor: Color(0xFF272C33),
  sideBarColor: Color(0xFF303840),
  onSideBarColor: Colors.white,
  onWarningColor: Colors.deepOrange,
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
  borderRadius: BorderRadius.all(Radius.circular(4)),
);

ThemeData _buildThemeData(ColorScheme colorScheme) {
  return ThemeData(
    useMaterial3: true,
    brightness: colorScheme.brightness,
    colorScheme: colorScheme,
    chipTheme: const ChipThemeData(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(90)),
      ),
    ),
    inputDecorationTheme: const InputDecorationTheme(border: OutlineInputBorder(), isDense: true),
    elevatedButtonTheme: ElevatedButtonThemeData(style: ElevatedButton.styleFrom(shape: _buttonBorder, minimumSize: const Size(0, 48))),
    filledButtonTheme: FilledButtonThemeData(style: FilledButton.styleFrom(shape: _buttonBorder, minimumSize: const Size(0, 48))),
    outlinedButtonTheme: OutlinedButtonThemeData(style: OutlinedButton.styleFrom(shape: _buttonBorder, minimumSize: const Size(0, 48))),
    textButtonTheme: TextButtonThemeData(style: TextButton.styleFrom(shape: _buttonBorder, minimumSize: const Size(0, 48))),
    //popupMenuTheme: PopupMenuThemeData(surfaceTintColor: colorScheme.surface, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8))),
    textTheme: const TextTheme(
      labelLarge: TextStyle(
        fontSize: 13,
        fontWeight: FontWeight.bold,
        fontFamily: 'HeaderFont',
      ),
    ),
  );
}

//Schemes
final _lightColorScheme = ColorScheme.fromSeed(
  seedColor: seedColor,
  brightness: Brightness.light,
  error: const Color(0xFF981111),
  onError: const Color(0xFFFFFFFF),
  surface: const Color(0xFFF5F7FA),
  onSurface: const Color(0xFF272C33),
).copyWith();

//Schemes
final _darkColorScheme = ColorScheme.fromSeed(
  seedColor: seedColor,
  brightness: Brightness.dark,
  error: const Color(0xFFEC0000),
  onError: const Color(0xFFFFFFFF),
  errorContainer: _lightColorScheme.errorContainer,
  onErrorContainer: _lightColorScheme.onErrorContainer,
  surface: const Color(0xFF1D2126),
  onSurface: const Color(0xFFF2F9FF),
).copyWith();
