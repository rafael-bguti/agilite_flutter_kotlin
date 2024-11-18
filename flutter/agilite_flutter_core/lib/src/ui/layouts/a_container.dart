import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AContainer extends StatelessWidget {
  final Widget child;
  final Widget? headerOptions;

  final bool fluid;
  final String? headerLabel;

  const AContainer({
    required this.child,
    this.headerOptions,
    this.headerLabel,
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
            _buildTaskHeader(width, screenSize),
            if (_needDivider())
              const Padding(
                padding: EdgeInsets.symmetric(vertical: 16.0),
                child: Divider(height: 1),
              ),
            SizedBox(
              width: fluid ? double.infinity : width,
              child: Padding(
                padding: EdgeInsets.only(
                  left: screenSize.horizontalPadding(),
                  right: screenSize.horizontalPadding(),
                ),
                child: child,
              ),
            ),
          ],
        ),
      ),
    );
  }

  bool _needDivider() {
    return headerLabel != null || headerOptions != null;
  }

  Widget _buildTaskHeader(double width, ScreenSize screenSize) {
    if (headerLabel == null && this.headerOptions == null) {
      return const SizedBox.shrink();
    }

    final headerText = _buildLabel(screenSize);
    final headerOptions = _buildHeaderOptions(screenSize);
    return SizedBox(
      width: fluid ? double.infinity : width,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          headerText,
          headerOptions,
        ],
      ),
    );
  }

  Widget _buildHeaderOptions(ScreenSize screenSize) {
    if (headerOptions != null) {
      return Padding(
        padding: EdgeInsets.only(
          right: screenSize.horizontalPadding(),
        ),
        child: headerOptions,
      );
    } else {
      return const SizedBox.shrink();
    }
  }

  Widget _buildLabel(ScreenSize screenSize) {
    if (headerLabel != null) {
      return Padding(
        padding: EdgeInsets.only(
          left: screenSize.horizontalPadding(),
        ),
        child: SelectableText(
          headerLabel!,
          style: textTheme?.headlineMedium?.copyWith(fontFamily: 'HeaderFont'),
        ),
      );
    } else {
      return const SizedBox.shrink();
    }
  }
}
