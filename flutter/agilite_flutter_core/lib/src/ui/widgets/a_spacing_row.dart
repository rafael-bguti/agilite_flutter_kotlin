import 'package:flutter/material.dart';

class ASpacingRow extends Row {
  ASpacingRow({
    super.key,
    super.mainAxisAlignment,
    super.mainAxisSize,
    super.crossAxisAlignment,
    super.textDirection,
    super.verticalDirection,
    super.textBaseline,
    List<Widget> children = const <Widget>[],
    double spacing = 8,
  }) : super(
          children: _addSpaces(children, spacing).toList(),
        );

  static Iterable<Widget> _addSpaces(List<Widget> childrens, double spacing) sync* {
    final spaceWidget = SizedBox(width: spacing);

    for (int i = 0; i < childrens.length; i++) {
      yield childrens[i];
      if (i < childrens.length - 1) {
        yield spaceWidget;
      }
    }
  }
}
