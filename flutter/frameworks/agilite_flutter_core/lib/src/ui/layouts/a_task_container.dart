import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ATaskContainer extends StatelessWidget {
  final AContainerHeader? header;
  final Widget child;
  final Widget? footer;

  final bool fluid;

  const ATaskContainer({
    required this.child,
    this.header,
    this.footer,
    this.fluid = false,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final screenSize = ScreenSize(context);
    const width = kMinWidthMenuFixed - kSidebarWidth;

    return Column(
      children: [
        Expanded(
          child: SingleChildScrollView(
            child: Center(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  if (header != null) header!.build(context, fluid, width),
                  if (_needHeaderDivider()) _buildLine(),
                  _buildSizedBox(child, screenSize),
                ],
              ),
            ),
          ),
        ),
        if (_needFooterDivider()) _buildLine(),
        if (footer != null) footer!,
      ],
    );
  }

  Widget _buildSizedBox(Widget child, ScreenSize screenSize) {
    return SizedBox(
      width: fluid ? double.infinity : kMinWidthMenuFixed - kSidebarWidth,
      child: Padding(
        padding: EdgeInsets.only(
          left: screenSize.horizontalPadding(),
          right: screenSize.horizontalPadding(),
          bottom: 32,
        ),
        child: child,
      ),
    );
  }

  Widget _buildLine() {
    return const Padding(
      padding: EdgeInsets.symmetric(vertical: 8.0),
      child: ADivider.lineOnly(),
    );
  }

  bool _needHeaderDivider() {
    return header != null;
  }

  bool _needFooterDivider() {
    return header != null;
  }
}

//
//   @override
//   Widget build(BuildContext context) {
//     final screenSize = ScreenSize(context);
//     const width = kMinWidthMenuFixed - kSidebarWidth;
//
//     final body = Column(
//       children: [
//         Column(
//           crossAxisAlignment: CrossAxisAlignment.center,
//           mainAxisAlignment: MainAxisAlignment.start,
//           children: [
//             if (header != null) header!.build(context, fluid, width),
//             if (_needDivider())
//               const Padding(
//                 padding: EdgeInsets.symmetric(vertical: 16.0),
//                 child: ADivider.lineOnly(),
//               ),
//             SizedBox(
//               width: fluid ? double.infinity : width,
//               child: Padding(
//                 padding: EdgeInsets.only(
//                   left: screenSize.horizontalPadding(),
//                   right: screenSize.horizontalPadding(),
//                 ),
//                 child: child,
//               ),
//             ),
//           ],
//         ),
//       ],
//     );
//
//     final wrapper = _buildWrapper(body, width);
//
//     return Align(
//       alignment: Alignment.topCenter,
//       child: wrapper,
//     );
//   }
//

//
//   Widget _buildWrapper(Widget body, double width) {
//     if (footer != null) {
//       return Stack(
//         fit: StackFit.expand,
//         children: [
//           Positioned.fill(
//             child: body,
//           ),
//           Positioned(
//             left: 0,
//             right: 0,
//             bottom: 0,
//             child: Center(
//               child: SizedBox(
//                 width: fluid ? double.infinity : width,
//                 child: footer!,
//               ),
//             ),
//           ),
//         ],
//       );
//     } else {
//       return body;
//     }
//   }
// }
//
class AContainerHeader {
  final Widget? headerOptions;

  final Widget? headerLabel;
  final String? headerText;

  const AContainerHeader.label(this.headerLabel, {this.headerOptions}) : headerText = null;

  const AContainerHeader.text(this.headerText, {this.headerOptions}) : headerLabel = null;

  Widget build(BuildContext context, bool fluid, double width) {
    if (headerLabel == null && headerText == null) {
      return const SizedBox.shrink();
    }

    final screenSize = ScreenSize(context);
    final headerLeft = _buildLabel(screenSize);
    final headerRight = _buildHeaderOptions(screenSize);

    return SizedBox(
      width: fluid ? double.infinity : width,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          headerLeft,
          const Spacer(),
          headerRight,
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
    if (headerText == null && headerLabel == null) {
      return const SizedBox.shrink();
    }

    Widget child = headerLabel != null
        ? headerLabel!
        : SelectableText(
            headerText!,
            style: textTheme?.headlineMedium?.copyWith(fontFamily: 'HeaderFont'),
          );

    return Padding(
      padding: EdgeInsets.only(
        left: screenSize.horizontalPadding(),
      ),
      child: child,
    );
  }
}
