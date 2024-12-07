import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ADivider extends StatelessWidget {
  final String? text;
  final Widget? label;

  final EdgeInsets padding;

  const ADivider.lineOnly({
    super.key,
    this.padding = const EdgeInsets.all(0),
  })  : text = null,
        label = null;

  const ADivider.label({
    required Widget this.label,
    this.padding = const EdgeInsets.symmetric(vertical: 16),
    super.key,
  }) : text = null;

  const ADivider.text({
    required String this.text,
    this.padding = const EdgeInsets.symmetric(vertical: 16),
    super.key,
  }) : label = null;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: padding,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          _buildLabel(),
          Expanded(
            child: Container(
              color: textTheme?.bodyMedium?.color?.withOpacity(0.2),
              height: 1,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildLabel() {
    final Widget? content;

    if (label != null) {
      content = label;
    } else if (text != null) {
      content = AText(
        text!.toUpperCase(),
        style: textTheme?.bodyMedium?.copyWith(
          fontFamily: 'HeaderFont',
          fontSize: 16,
          letterSpacing: 1.3,
        ),
      );
    } else {
      return const SizedBox.shrink();
    }

    return Padding(
      padding: const EdgeInsets.only(right: 8),
      child: content,
    );
  }
}
