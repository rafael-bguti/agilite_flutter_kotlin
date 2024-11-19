import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AForm extends StatefulWidget {
  final AFormController controller;
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
  late final AFormController _controller;

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

  T addController<T extends FieldController<dynamic>>(T fieldController) {
    return _controller.addController(fieldController);
  }

  ValueNotifier<List<String>> get $validationMessages => _controller.$validationMessages;
  void registerPanelValidationGlobalKey(GlobalKey key) {
    _controller.panelValidationGlobalKey = key;
  }

  FieldController<dynamic>? getControllerByName(String name) {
    return _controller.getControllerByName(name);
  }
}
