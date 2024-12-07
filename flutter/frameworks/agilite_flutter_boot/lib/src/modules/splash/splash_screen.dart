import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class SplashScreen extends StatefulWidget {
  final SplashService? service;
  const SplashScreen({this.service, super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  late final SplashService _service = widget.service ?? SplashService(authService);

  @override
  void initState() {
    super.initState();
    _service.loadNextRoute().then((route) {
      ANavigator.replace(route);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: coreStyle.loadingWidget,
      ),
    );
  }
}
