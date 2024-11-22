import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/style/style_helper.dart' as static_theme;
import 'package:flutter/material.dart';

class ASideDialog {
  static Future<dynamic> showRight({
    required Widget Function(BuildContext context) builder,
    double? width,
    double? height,
    Color? barrierColor,
    double sheetBorderRadius = 8,
    Color? sheetColor,
    BoxConstraints? constraints,
    EdgeInsetsGeometry? padding,
    Duration transitionDuration = kThemeAnimationDuration,
    VoidCallback? onVisible,
    bool barrierDismissible = true,
  }) async {
    return _show(
      builder: builder,
      width: width ?? MediaQuery.of(globalNavigatorKey.currentContext!).size.width * (PlatformInfo.isMobile ? 1 : 0.5),
      offset: const Offset(1, 0),
      borderRadius: BorderRadius.only(topLeft: Radius.circular(sheetBorderRadius), bottomLeft: Radius.circular(sheetBorderRadius)),
      padding: padding ?? const EdgeInsets.symmetric(horizontal: 0.0, vertical: 8),
      height: height,
      alignment: Alignment.centerRight,
      barrierColor: barrierColor,
      sheetBorderRadius: sheetBorderRadius,
      sheetColor: sheetColor,
      constraints: constraints,
      transitionDuration: transitionDuration,
      onVisible: onVisible,
      barrierDismissible: barrierDismissible,
    );
  }

  static Future<dynamic> showLeft({
    required Widget Function(BuildContext context) builder,
    double? width,
    double? height,
    Color? barrierColor,
    double sheetBorderRadius = 8,
    Color? sheetColor,
    BoxConstraints? constraints,
    EdgeInsetsGeometry? padding,
    Duration transitionDuration = kThemeAnimationDuration,
    VoidCallback? onVisible,
    bool barrierDismissible = true,
  }) async {
    return _show(
      builder: builder,
      width: width ?? MediaQuery.of(globalNavigatorKey.currentContext!).size.width * (PlatformInfo.isMobile ? 1 : 0.5),
      offset: const Offset(-1, 0),
      borderRadius: BorderRadius.only(topRight: Radius.circular(sheetBorderRadius), bottomRight: Radius.circular(sheetBorderRadius)),
      padding: padding ?? const EdgeInsets.symmetric(horizontal: 0.0, vertical: 8),
      alignment: Alignment.centerLeft,
      height: height,
      barrierColor: barrierColor,
      sheetBorderRadius: sheetBorderRadius,
      sheetColor: sheetColor,
      constraints: constraints,
      transitionDuration: transitionDuration,
      onVisible: onVisible,
      barrierDismissible: barrierDismissible,
    );
  }

  static Future<dynamic> showBottom({
    required Widget Function(BuildContext context) builder,
    double? width,
    double? height,
    Color? barrierColor,
    double sheetBorderRadius = 8,
    Color? sheetColor,
    BoxConstraints? constraints,
    EdgeInsetsGeometry? padding,
    Duration transitionDuration = kThemeAnimationDuration,
    VoidCallback? onVisible,
    bool barrierDismissible = true,
  }) async {
    return _show(
      builder: builder,
      width: width ?? MediaQuery.of(globalNavigatorKey.currentContext!).size.width,
      offset: const Offset(0, 1),
      borderRadius: BorderRadius.only(topRight: Radius.circular(sheetBorderRadius), topLeft: Radius.circular(sheetBorderRadius)),
      padding: padding ?? const EdgeInsets.only(top: 24, left: 8, right: 8),
      alignment: Alignment.bottomCenter,
      height: height,
      barrierColor: barrierColor,
      sheetBorderRadius: sheetBorderRadius,
      sheetColor: sheetColor,
      constraints: constraints,
      transitionDuration: transitionDuration,
      onVisible: onVisible,
      barrierDismissible: barrierDismissible,
    );
  }

  static Future<dynamic> _show({
    required Alignment alignment,
    required Widget Function(BuildContext context) builder,
    required double width,
    required Offset offset,
    required BorderRadiusGeometry borderRadius,
    required EdgeInsetsGeometry padding,
    double? height,
    Color? barrierColor,
    double sheetBorderRadius = 8,
    Color? sheetColor,
    BoxConstraints? constraints,
    Duration transitionDuration = kThemeAnimationDuration,
    VoidCallback? onVisible,
    bool barrierDismissible = true,
  }) {
    return showGeneralDialog(
      barrierDismissible: barrierDismissible,
      barrierLabel: 'Fechar',
      barrierColor: barrierColor ?? static_theme.barrierColor,
      transitionDuration: transitionDuration,
      useRootNavigator: true,
      context: globalNavigatorKey.currentContext!,
      pageBuilder: (context, animation1, animation2) {
        if (onVisible != null) runOnNextBuild(onVisible);

        return Padding(
          padding: padding,
          child: Align(
            alignment: alignment,
            child: Container(
              height: height ?? MediaQuery.of(context).size.height,
              decoration: BoxDecoration(
                color: sheetColor ?? static_theme.backgroundColor,
                borderRadius: borderRadius,
              ),
              padding: const EdgeInsets.all(8.0),
              constraints: constraints,
              width: width,
              child: builder(context),
            ),
          ),
        );
      },
      transitionBuilder: (context, animation1, animation2, child) {
        return SlideTransition(
          position: Tween(begin: offset, end: const Offset(0, 0)).animate(animation1),
          child: child,
        );
      },
    );
  }
}
