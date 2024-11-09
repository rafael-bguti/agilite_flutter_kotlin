import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ASeparator extends StatelessWidget {
  final String label;

  const ASeparator({
    required this.label,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 16.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.only(right: 8),
            child: AText(
              label.toUpperCase(),
              style: textTheme?.bodyMedium?.copyWith(
                fontFamily: 'HeaderFont',
                fontSize: 16,
                letterSpacing: 1.3,
              ),
            ),
          ),
          Expanded(
            child: Divider(
              color: textTheme?.bodyMedium?.color,
              thickness: 0.5,
            ),
          ),
        ],
      ),
    );
  }
}
