import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ATextFieldFone extends StatelessWidget {
  final String name;
  final String? labelText;

  const ATextFieldFone(
    this.name, {
    this.labelText,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return ATextField.string(
      name,
      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
      onControllerCreated: (controller) => _configureController(controller as StringController),
      labelText: labelText,
    );
  }

  void _configureController(StringController controller) {
    controller.focusNode.addListener(() {
      if (!controller.focusNode.hasFocus) {
        controller.value = controller.value;
      }
    });

    controller.parseValue = (text) => text.onlyNumbers();
    controller.formatValue = _formatFone;
  }

  String _formatFone(final String? fone) {
    return formatFone(fone) ?? '';
  }
}
