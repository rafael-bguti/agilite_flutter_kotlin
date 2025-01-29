import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AForm extends StatefulWidget {
  final FormController controller;
  final Map<String, dynamic>? value;
  final Widget child;

  const AForm(
    this.controller, {
    this.value,
    super.key,
    required this.child,
  });

  @override
  State<AForm> createState() => AFormState();
}

class AFormState extends State<AForm> {
  late final FormController _controller;

  @override
  void initState() {
    super.initState();
    _controller = widget.controller;
  }

  @override
  void didUpdateWidget(AForm oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (oldWidget.value != widget.value) {
      _controller.value = widget.value;
    }
  }

  @override
  Widget build(BuildContext context) {
    return widget.child;
  }

  ValueNotifier<List<String>> get $validationMessages => _controller.$validationMessages;
  void registerPanelValidationGlobalKey(GlobalKey key) {
    _controller.panelValidationGlobalKey = key;
  }

  FormController get controller => _controller;
}
