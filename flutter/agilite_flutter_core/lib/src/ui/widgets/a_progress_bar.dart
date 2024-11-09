import 'dart:math';

import 'package:flutter/material.dart';

class AProgressBar extends StatelessWidget {
  final String label;
  final String? rightLabel;
  final int value;
  final int maxValue;
  final bool showValue;
  final Color barColor;
  const AProgressBar({
    required this.label,
    required this.value,
    this.rightLabel,
    this.maxValue = 100,
    this.barColor = Colors.blue,
    this.showValue = true,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(label, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
            if (rightLabel != null) Text(rightLabel!, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w300)), // Mostra o valor ao lado
            if (rightLabel == null && showValue) Text(value.toString(), style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w300)), // Mostra o valor ao lado
          ],
        ),
        LinearProgressIndicator(
          value: max(min(value / maxValue, 1), 0), // Progresso proporcional
          backgroundColor: Colors.grey[300],
          color: barColor,
        ),
      ],
    );
  }
}
