import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ATextFieldNi extends StatelessWidget {
  final String name;
  final String? labelText;
  final bool? req;

  const ATextFieldNi(
    this.name, {
    this.labelText,
    this.req,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return ATextField.string(
      name,
      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
      onControllerCreated: (controller) => _configureController(controller as StringController),
      labelText: labelText,
      req: req == true,
    );
  }

  void _configureController(StringController controller) {
    controller.focusNode.addListener(() {
      if (!controller.focusNode.hasFocus) {
        controller.value = controller.value;
      }
    });
    controller.parseValue = (text) => text.onlyNumbers();
    controller.formatValue = (ni) => ni?.formatCpfCNPJ() ?? '';
  }
}
