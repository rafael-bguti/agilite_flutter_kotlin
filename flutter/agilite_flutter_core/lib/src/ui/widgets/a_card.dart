import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/style/style_helper.dart' as style;
import 'package:flutter/material.dart';

class ACard extends StatelessWidget {
  final Widget? header;
  final Widget body;

  final double? minHeight;
  final EdgeInsetsGeometry? padding;
  final bool showDivider;
  final bool showShadow;

  final Color? borderColor;
  final Color? backgroundColor;
  final Color? leftBorderColor;

  const ACard({
    required this.body,
    this.header,
    this.padding,
    this.showDivider = true,
    this.showShadow = true,
    this.minHeight,
    this.borderColor,
    this.backgroundColor,
    this.leftBorderColor,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final localBackgroundColor = backgroundColor ?? (brightness == Brightness.light ? Colors.white : const Color(0xFF23282e));
    final localPadding = padding ?? const EdgeInsets.all(16);
    final localBorderColor = borderColor ?? style.backgroundColor.darkenIfLightOrLightenIfDark(style.brightness, 0.1);

    final content = Container(
      decoration: BoxDecoration(
        color: localBackgroundColor,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: localBorderColor),
        boxShadow: showShadow
            ? [
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
              ]
            : null,
      ),
      constraints: BoxConstraints(minHeight: minHeight ?? 0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          if (header != null)
            Padding(
              padding: localPadding,
              child: header!,
            ),
          if (showDivider && header != null) const Divider(height: 1),
          Padding(
            padding: localPadding,
            child: body,
          ),
        ],
      ),
    );

    if (leftBorderColor == null) return content;

    return Container(
      decoration: BoxDecoration(
        color: leftBorderColor,
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(8),
          bottomLeft: Radius.circular(8),
          topRight: Radius.circular(16),
          bottomRight: Radius.circular(16),
        ),
      ),
      padding: const EdgeInsets.only(left: 4),
      child: content,
    );
  }
}
