import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ATitleSubtitle extends StatelessWidget {
  final String title;
  final String subtitle;

  final Axis? direction;
  final TextStyle? titleStyle;
  final TextStyle? subtitleStyle;

  const ATitleSubtitle({
    required this.title,
    required this.subtitle,
    this.direction = Axis.vertical,
    this.titleStyle,
    this.subtitleStyle,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final children = [
      AText(
        title,
        style: titleStyle ?? textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.bold),
      ),
      AText(
        subtitle,
        style: subtitleStyle ?? textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.normal),
      ),
    ];

    return direction == Axis.vertical
        ? Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.start,
            children: children,
          )
        : Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: children,
          );
  }
}
