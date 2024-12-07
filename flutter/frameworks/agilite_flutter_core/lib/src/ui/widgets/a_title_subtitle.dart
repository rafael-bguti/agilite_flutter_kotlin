import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ATitleSubtitle extends StatelessWidget {
  final String title;
  final String subtitle;

  const ATitleSubtitle({
    required this.title,
    required this.subtitle,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        AText(
          title,
          style: textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.bold),
        ),
        AText(
          subtitle,
          style: textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.normal),
        ),
      ],
    );
  }
}
