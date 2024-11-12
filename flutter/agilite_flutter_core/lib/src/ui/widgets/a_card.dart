import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/style/style_helper.dart' as style;
import 'package:flutter/material.dart';

class ACard extends StatelessWidget {
  final Widget? header;
  final Widget child;

  final double? minHeight;
  final EdgeInsetsGeometry? padding;
  final bool showDivider;
  final bool showShadow;

  final Color? borderColor;
  final Color? backgroundColor;
  final Color? leftBorderColor;

  const ACard({
    required this.child,
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
    final shadown = _buildShadown();

    final content = Container(
      decoration: BoxDecoration(
        color: localBackgroundColor,
        borderRadius: _getBorderRadius(leftBorderColor == null ? 4 : 0, 4),
        border: Border.all(color: localBorderColor),
        boxShadow: leftBorderColor == null ? shadown : null,
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
            child: child,
          ),
        ],
      ),
    );

    if (leftBorderColor == null) return content;

    return Container(
      decoration: BoxDecoration(
        color: leftBorderColor,
        borderRadius: _getBorderRadius(4, 16),
        boxShadow: leftBorderColor == null ? null : shadown,
      ),
      padding: const EdgeInsets.only(left: 8),
      child: content,
    );
  }

  BorderRadius _getBorderRadius(double left, double right) {
    return BorderRadius.only(
      topLeft: Radius.circular(left),
      bottomLeft: Radius.circular(left),
      topRight: Radius.circular(right),
      bottomRight: Radius.circular(right),
    );
  }

  List<BoxShadow>? _buildShadown() {
    return showShadow
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
        : null;
  }
}
