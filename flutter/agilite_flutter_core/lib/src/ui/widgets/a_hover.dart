import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AHover extends StatefulWidget {
  final Widget Function(bool hover) builder;
  const AHover({required this.builder, super.key});

  @override
  State<AHover> createState() => _AHoverState();
}

class _AHoverState extends State<AHover> {
  bool _hovered = false;
  @override
  Widget build(BuildContext context) {
    if (!PlatformInfo.hasMouse) {
      return widget.builder(false);
    }

    return MouseRegion(
      cursor: SystemMouseCursors.click,
      onEnter: (_) {
        setState(() {
          _hovered = true;
        });
      },
      onExit: (_) {
        setState(() {
          _hovered = false;
        });
      },
      child: widget.builder(_hovered),
    );
  }
}
