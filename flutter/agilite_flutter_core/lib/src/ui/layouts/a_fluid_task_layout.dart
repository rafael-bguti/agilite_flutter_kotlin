import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AFluidTaskLayout extends StatelessWidget {
  final Widget child;
  final String? taskHeader;

  const AFluidTaskLayout({
    required this.child,
    this.taskHeader,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final screenSize = ScreenSize(context);
    const width = kMinWidthMenuFixed - kSidebarWidth;
    return Align(
      alignment: Alignment.topCenter,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          ..._buildTaskHeader(width, screenSize),
          Expanded(
            child: SizedBox(
              width: width,
              child: Padding(
                padding: EdgeInsets.only(
                  left: screenSize.horizontalPadding(),
                  right: screenSize.horizontalPadding(),
                  top: 16,
                ),
                child: SingleChildScrollView(
                  child: child,
                ),
              ),
            ),
          ),
        ],
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
        width: width,
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
