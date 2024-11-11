import 'package:flutter/widgets.dart';

class AConsumer<T extends ChangeNotifier> extends StatefulWidget {
  final T notifier;
  final Widget Function(BuildContext context, T notifier) builder;
  final Widget? child;

  const AConsumer(
    this.notifier, {
    required this.builder,
    this.child,
    super.key,
  });

  @override
  State<AConsumer<T>> createState() => _AConsumerState<T>();
}

class _AConsumerState<T extends ChangeNotifier> extends State<AConsumer<T>> {
  @override
  void initState() {
    super.initState();
    widget.notifier.addListener(_refresh);
  }

  @override
  void dispose() {
    widget.notifier.removeListener(_refresh);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (widget.child != null) return widget.child!;

    return widget.builder(context, widget.notifier);
  }

  void _refresh() {
    setState(() {});
  }
}
