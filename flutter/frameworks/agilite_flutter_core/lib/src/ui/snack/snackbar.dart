import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

void showWarningSnack(final String message) {
  ScaffoldMessenger.of(globalNavigatorKey.currentState!.context).showSnackBar(
    _buildSnackBar(
      message,
      warningColor,
      onWarningColor,
      Icons.warning,
    ),
  );
}

void showErrorSnack(final String message) {
  ScaffoldMessenger.of(globalNavigatorKey.currentState!.context).showSnackBar(
    _buildSnackBar(
      message,
      errorColor,
      onWarningColor,
      Icons.error,
    ),
  );
}

void showSuccessSnack(final String message) {
  ScaffoldMessenger.of(globalNavigatorKey.currentState!.context).showSnackBar(
    _buildSnackBar(
      message,
      successColor,
      onSuccessColor,
      Icons.check_sharp,
    ),
  );
}

SnackBar _buildSnackBar(
  final String text,
  Color backgroundColor,
  Color onBackgroundColor,
  IconData icon,
) {
  return SnackBar(
    backgroundColor: backgroundColor,
    content: ASpacingRow(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(
          icon,
          color: onWarningColor,
          size: 40,
        ),
        Flexible(
          child: Text(
            text,
            textAlign: TextAlign.center,
            style: textTheme?.titleMedium?.copyWith(color: onBackgroundColor),
          ),
        ),
      ],
    ),
  );
}
