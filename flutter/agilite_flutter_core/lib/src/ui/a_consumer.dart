import 'package:flutter/widgets.dart';

class AConsumer<T extends ChangeNotifier> extends StatefulWidget {
  final T notifier;
  final Widget Function(BuildContext context, T notifier, Widget? child) builder;
  final Widget? child;

  const AConsumer({
    required this.notifier,
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
    return widget.builder(context, widget.notifier, widget.child);
  }

  void _refresh() {
    setState(() {});
  }
}
