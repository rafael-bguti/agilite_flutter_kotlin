import 'package:flutter/material.dart';

//-------- Buttons --------
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
