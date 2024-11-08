import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACard extends StatelessWidget {
  final double? minHeight;
  final Widget child;
  const ACard({required this.child, this.minHeight, super.key});

  @override
  Widget build(BuildContext context) {
    final color = brightness == Brightness.light ? Colors.white : const Color(0xFF23282e);

    return Container(
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.black.withOpacity(0.1)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            offset: const Offset(0, 3),
            blurRadius: 3,
            spreadRadius: -2,
          ),
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            offset: const Offset(0, 3),
            blurRadius: 4,
            spreadRadius: 0,
          ),
          BoxShadow(
            color: Colors.black.withOpacity(0.02),
            offset: const Offset(0, 1),
            blurRadius: 8,
            spreadRadius: 0,
          ),
        ],
      ),
      constraints: BoxConstraints(minHeight: minHeight ?? 0),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: child,
      ),
    );
  }
}
