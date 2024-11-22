import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/ui/views/a_view_controller.dart';
import 'package:flutter/material.dart';

class AView<T> extends StatefulWidget {
  final AViewController<T> controller;
  final Widget Function(BuildContext context, T state) builder;
  const AView({
    required this.controller,
    required this.builder,
    super.key,
  });

  @override
  State<AView<T>> createState() => _AViewState();
}

class _AViewState<T> extends State<AView<T>> {
  late final AViewController<T> controller = widget.controller;

  @override
  void initState() {
    super.initState();
    runOnNextBuild(() {
      controller.onViewLoaded();
    });
  }

  @override
  Widget build(BuildContext context) {
    return AConsumer(
      notifier: controller,
      builder: (context, __, ___) {
        if (controller.hasState) {
          return widget.builder(context, controller.state);
        } else {
          return const SizedBox.shrink();
        }
      },
    );
  }
}
