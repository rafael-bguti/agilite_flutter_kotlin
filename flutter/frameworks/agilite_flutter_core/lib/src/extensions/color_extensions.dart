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

  Color lightenOrDarken(Brightness brightness, [double amount = .1]) {
    assert(amount >= 0 && amount <= 1);

    if (brightness == Brightness.light) {
      return lighten(amount);
    } else {
      return darken(amount);
    }
  }

  Color get contrastingColor {
    double luminance = (0.299 * red / 255) + (0.587 * green / 255) + (0.114 * blue / 255);
    return luminance > 0.5 ? Colors.black : Colors.white;
  }
}
