import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ATag extends StatelessWidget {
  final String text;
  final Color? color;
  const ATag({
    required this.text,
    this.color,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final localColor = color ?? Theme.of(context).primaryColor;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: localColor,
        borderRadius: BorderRadius.circular(4),
      ),
      child: Text(
        text,
        style: TextStyle(
          color: localColor.contrastingColor,
          fontSize: 12,
        ),
      ),
    );
  }
}
