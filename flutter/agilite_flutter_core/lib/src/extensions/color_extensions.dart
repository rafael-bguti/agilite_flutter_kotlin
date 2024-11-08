import 'package:flutter/material.dart';

extension ColorExtensions on Color {
  Color darken([double amount = .1]) {
    assert(amount >= 0 && amount <= 1);

    final hsl = HSLColor.fromColor(this);
    final hslDark = hsl.withLightness((hsl.lightness - amount).clamp(0.0, 1.0));

    return hslDark.toColor();
  }

  Color lighten([double amount = .1]) {
    assert(amount >= 0 && amount <= 1);

    final hsl = HSLColor.fromColor(this);
    final hslLight = hsl.withLightness((hsl.lightness + amount).clamp(0.0, 1.0));

    return hslLight.toColor();
  }

  Color darkenIfLightOrLightenIfDark(Brightness brightness, [double amount = .1]) {
    assert(amount >= 0 && amount <= 1);

    if (brightness == Brightness.light) {
      return darken(amount);
    } else {
      return lighten(amount);
    }
  }
}