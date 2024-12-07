// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';

enum LogoDestination { appBar, drawer }

//TODO colocar essa classe como o extensions do Thema e não a CoreStyleColors, assim todas as propriedades ficam como extensions
class CoreStyle {
  final Widget loadingWidget;
  final Widget Function(LogoDestination destination, Brightness brightness)? logoBuilder;

  final double kPaddingS;
  final double kPaddingM;
  final double kPaddingL;

  final String? assetLoginBackground;

  const CoreStyle({
    Widget? loadingWidget,
    this.logoBuilder,
    BoxDecoration? tableHeaderRowDecoration,
    BoxDecoration? tableRowDecoration,
    this.kPaddingS = 8,
    this.kPaddingM = 12,
    this.kPaddingL = 16,
    this.assetLoginBackground = 'packages/agilite_flutter_core/assets/login_background.jpg',
  }) : loadingWidget = loadingWidget ?? const DefaultLoadingWidget();

  Widget getLogoWidget(LogoDestination destination, Brightness brightness) {
    if (logoBuilder != null) {
      return logoBuilder!(destination, brightness);
    }

    final logo = brightness == Brightness.dark ? 'logow' : 'logob';
    return Image(
      image: AssetImage('assets/images/$logo.png'),
      height: destination == LogoDestination.appBar ? 18 : 40,
    );
  }
}

class CoreStyleColors extends ThemeExtension<CoreStyleColors> {
  final String name;

  final Color successColor;
  final Color onSuccessColor;

  final Color warningColor;
  final Color onWarningColor;

  final Color appBarColor;
  final Color onAppBarColor;

  final Color sideBarColor;
  final Color onSideBarColor;

  const CoreStyleColors({
    required this.name,
    required this.successColor,
    required this.onSuccessColor,
    required this.appBarColor,
    required this.onAppBarColor,
    required this.sideBarColor,
    required this.onSideBarColor,
    this.warningColor = const Color(0xFFFAEED8),
    this.onWarningColor = const Color(0xFF1D2126),
  });

  @override
  ThemeExtension<CoreStyleColors> copyWith({
    Color? successColor,
    Color? onSuccessColor,
    Color? warningColor,
    Color? onWarningColor,
    Color? appBarColor,
    Color? onAppBarColor,
    Color? sideBarColor,
    Color? onSideBarColor,
  }) {
    return CoreStyleColors(
      name: name,
      successColor: successColor ?? this.successColor,
      onSuccessColor: onSuccessColor ?? this.onSuccessColor,
      warningColor: warningColor ?? this.warningColor,
      onWarningColor: onWarningColor ?? this.onWarningColor,
      appBarColor: appBarColor ?? this.appBarColor,
      onAppBarColor: onAppBarColor ?? this.onAppBarColor,
      sideBarColor: sideBarColor ?? this.sideBarColor,
      onSideBarColor: onSideBarColor ?? this.onSideBarColor,
    );
  }

  @override
  ThemeExtension<CoreStyleColors> lerp(covariant ThemeExtension<CoreStyleColors>? other, double t) {
    if (other is! CoreStyleColors) {
      return this;
    }
    return CoreStyleColors(
      name: name,
      successColor: Color.lerp(successColor, other.successColor, t)!,
      onSuccessColor: Color.lerp(onSuccessColor, other.onSuccessColor, t)!,
      warningColor: Color.lerp(warningColor, other.warningColor, t)!,
      onWarningColor: Color.lerp(onWarningColor, other.onWarningColor, t)!,
      appBarColor: Color.lerp(appBarColor, other.appBarColor, t)!,
      onAppBarColor: Color.lerp(onAppBarColor, other.onAppBarColor, t)!,
      sideBarColor: Color.lerp(sideBarColor, other.sideBarColor, t)!,
      onSideBarColor: Color.lerp(onSideBarColor, other.onSideBarColor, t)!,
    );
  }
}

class DefaultLoadingWidget extends StatelessWidget {
  const DefaultLoadingWidget({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.transparent,
      width: 180,
      height: 180,
      child: Lottie.asset(
        'packages/agilite_flutter_core/assets/loading.json',
        repeat: true,
      ),
    );
  }
}
