import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AFieldset extends StatelessWidget {
  final String? title;
  final Color? titleColor;
  final Widget child;

  const AFieldset({
    this.title,
    this.titleColor,
    required this.child,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 8.0),
      child: InputDecorator(
        decoration: InputDecoration(
          contentPadding: const EdgeInsets.all(12),
          label: AText(
            title!.toUpperCase(),
            style: textTheme?.bodyMedium?.copyWith(
              fontFamily: 'HeaderFont',
              fontSize: 16,
              letterSpacing: 1.3,
            ),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: primaryColor,
              width: 0.25,
            ),
          ),
          fillColor: Colors.transparent,
        ),
        child: Padding(
          padding: const EdgeInsets.only(top: 8.0),
          child: child,
        ),
      ),
    );
  }
}
