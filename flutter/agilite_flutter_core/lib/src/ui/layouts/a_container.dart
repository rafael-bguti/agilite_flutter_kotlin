import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AContainer extends StatelessWidget {
  final Widget child;

  final bool fluid;
  final String? taskHeader;

  const AContainer({
    required this.child,
    this.taskHeader,
    this.fluid = false,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final screenSize = ScreenSize(context);
    const width = kMinWidthMenuFixed - kSidebarWidth;
    return Padding(
      padding: const EdgeInsets.only(bottom: 24.0),
      child: Align(
        alignment: Alignment.topCenter,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            ..._buildTaskHeader(width, screenSize),
            SizedBox(
              width: fluid ? double.infinity : width,
              child: Padding(
                padding: EdgeInsets.only(
                  left: screenSize.horizontalPadding(),
                  right: screenSize.horizontalPadding(),
                  top: 16,
                ),
                child: child,
              ),
            ),
          ],
        ),
      ),
    );
  }

  List<Widget> _buildTaskHeader(double width, ScreenSize screenSize) {
    final List<Widget> children = [];
    if (taskHeader == null) {
      return children;
    }
    children.add(
      SizedBox(
        width: fluid ? double.infinity : width,
        child: Padding(
          padding: EdgeInsets.only(
            left: screenSize.horizontalPadding(),
            right: screenSize.horizontalPadding(),
            bottom: 16,
          ),
          child: SelectableText(
            taskHeader!,
            style: textTheme?.headlineMedium?.copyWith(fontFamily: 'HeaderFont'),
          ),
        ),
      ),
    );
    children.add(const Divider(height: 1));

    return children;
  }
}
