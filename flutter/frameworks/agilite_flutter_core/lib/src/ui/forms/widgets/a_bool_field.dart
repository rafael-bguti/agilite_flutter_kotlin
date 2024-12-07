import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

enum ABoolRenderType {
  checkbox,
  switcher,
}

enum LabelOrientation {
  horizontal,
  vertical,
}

//TODO o component está com algum BUG que não exibe o FOcus no primeiro foco
class ABoolField extends StatefulWidget {
  final ABoolRenderType renderType;

  final LabelOrientation? labelOrientation;

  //Criado via controller
  final BoolController? fieldController;

  //Parâmetros para criar o controller aqui no Checkbox
  final String? name;
  final String? labelText;
  final bool? enabled;
  final bool? initialValue;
  final bool? defaultValue;
  final bool? autoFocus;

  final void Function(BoolController controller)? onControllerCreated;

  const ABoolField.controller(
    BoolController this.fieldController, {
    super.key,
    this.renderType = ABoolRenderType.switcher,
    this.autoFocus,
    this.labelOrientation = LabelOrientation.horizontal,
  })  : name = null,
        labelText = null,
        enabled = null,
        initialValue = null,
        defaultValue = null,
        onControllerCreated = null;

  const ABoolField(
    String this.name, {
    this.renderType = ABoolRenderType.switcher,
    super.key,
    this.labelText,
    this.enabled,
    this.initialValue,
    this.defaultValue,
    this.autoFocus,
    this.labelOrientation = LabelOrientation.horizontal,
    this.onControllerCreated,
  }) : fieldController = null;

  @override
  State<ABoolField> createState() => _ABoolFieldState();
}

class _ABoolFieldState extends State<ABoolField> with FieldControllerRegisterMixin {
  late final BoolController fieldController;

  @override
  void initState() {
    super.initState();
    fieldController = registerFormField(context, widget.fieldController, widget.name, _buildController, widget.onControllerCreated);
  }

  @override
  Widget build(BuildContext context) {
    final enabled = fieldController.enabled ?? true;
    final theme = Theme.of(context);

    return AConsumer(
      notifier: fieldController,
      builder: (context, fieldController, _) {
        final childs = [
          if (widget.renderType == ABoolRenderType.switcher)
            Switch(
              focusNode: fieldController.focusNode,
              value: fieldController.value,
              onChanged: !enabled ? null : (value) => fieldController.value = value,
            ),
          if (widget.renderType == ABoolRenderType.checkbox)
            Checkbox(
              focusNode: fieldController.focusNode,
              value: fieldController.value,
              onChanged: !enabled ? null : (value) => fieldController.value = value,
            ),
          if (fieldController.labelText != null)
            Text(
              fieldController.labelText!,
              style: theme.inputDecorationTheme.labelStyle,
            ),
        ];

        final component = widget.labelOrientation == LabelOrientation.horizontal
            ? Row(
                mainAxisSize: MainAxisSize.min,
                children: childs,
              )
            : Column(
                mainAxisSize: MainAxisSize.min,
                children: childs,
              );

        return InkWell(
          hoverColor: Colors.transparent,
          onTap: enabled ? () => fieldController.value = !fieldController.value : null,
          child: component,
        );
      },
    );
  }

  BoolController _buildController() {
    return BoolController(
      widget.name!,
      labelText: widget.labelText,
      enabled: widget.enabled,
      initialValue: widget.initialValue,
      defaultValue: widget.defaultValue ?? false,
      autoFocus: widget.autoFocus ?? false,
    );
  }
}
