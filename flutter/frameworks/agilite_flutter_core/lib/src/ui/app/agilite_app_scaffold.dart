import 'package:agilite_flutter_core/src/style/style_helper.dart';
import 'package:flutter/material.dart';

import 'app_scroll_behavior.dart';

class AgiliteAppScaffold extends StatelessWidget {
  const AgiliteAppScaffold({
    super.key,
    required this.child,
  });
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return DefaultTextStyle(
      style: textTheme!.bodyMedium!,
      child: ScrollConfiguration(
        behavior: AppScrollBehavior(),
        child: Scaffold(body: child),
      ),
    );
  }
}
