import 'package:flutter/material.dart';

class ASpacingRow extends Row {
  ASpacingRow({
    Key? key,
    MainAxisAlignment mainAxisAlignment = MainAxisAlignment.start,
    MainAxisSize mainAxisSize = MainAxisSize.max,
    CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.center,
    TextDirection? textDirection,
    VerticalDirection verticalDirection = VerticalDirection.down,
    TextBaseline? textBaseline,
    List<Widget> children = const <Widget>[],
    double spacing = 8,
  }) : super(
          children: _addSpaces(children, spacing).toList(),
          key: key,
          mainAxisAlignment: mainAxisAlignment,
          mainAxisSize: mainAxisSize,
          crossAxisAlignment: crossAxisAlignment,
          textDirection: textDirection,
          verticalDirection: verticalDirection,
          textBaseline: textBaseline,
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
