import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/style/style_helper.dart' as static_theme;
import 'package:flutter/material.dart';

enum SheetSide {
  left,
  right,
}

class ASideDialog {
  static Future<dynamic> show({
    required Widget Function(BuildContext context) builder,
    double? width,
    double? height,
    SheetSide side = SheetSide.right,
    Color? barrierColor,
    double sheetBorderRadius = 8,
    Color? sheetColor,
    BoxConstraints? constraints,
    EdgeInsetsGeometry? padding,
    Duration transitionDuration = kThemeAnimationDuration,
    VoidCallback? onVisible,
  }) async {
    dynamic data = await _showSheetSide(
      builder: builder,
      width: width,
      height: height,
      side: side,
      barrierColor: barrierColor ?? static_theme.barrierColor,
      sheetBorderRadius: sheetBorderRadius,
      sheetColor: sheetColor ?? backgroundColor,
      transitionDuration: transitionDuration,
      constraints: constraints,
      padding: padding,
      onVisible: onVisible,
    );

    return data;
  }

  static Future<dynamic> _showSheetSide({
    required Widget Function(BuildContext context) builder,
    required SheetSide side,
    double? width,
    double? height,
    required Color barrierColor,
    required double sheetBorderRadius,
    required Color sheetColor,
    required Duration transitionDuration,
    BoxConstraints? constraints,
    EdgeInsetsGeometry? padding,
    VoidCallback? onVisible,
  }) {
    return showGeneralDialog(
      barrierDismissible: true,
      barrierLabel: 'Fechar',
      barrierColor: barrierColor,
      transitionDuration: transitionDuration,
      useRootNavigator: true,
      context: globalNavigatorKey.currentContext!,
      pageBuilder: (context, animation1, animation2) {
        if (onVisible != null) runOnNextBuild(onVisible);

        return Padding(
          padding: padding ?? const EdgeInsets.all(0.0),
          child: Align(
            alignment: (side == SheetSide.right ? Alignment.centerRight : Alignment.centerLeft),
            child: Material(
              elevation: 15,
              color: Colors.transparent,
              child: Container(
                decoration: BoxDecoration(color: sheetColor),
                height: height ?? double.infinity,
                constraints: constraints,
                width: width ?? MediaQuery.of(context).size.width * 0.5,
                child: builder(context),
              ),
            ),
          ),
        );
      },
      transitionBuilder: (context, animation1, animation2, child) {
        return SlideTransition(
          position: Tween(begin: Offset((side == SheetSide.right ? 1 : -1), 0), end: const Offset(0, 0)).animate(animation1),
          child: child,
        );
      },
    );
  }
}
