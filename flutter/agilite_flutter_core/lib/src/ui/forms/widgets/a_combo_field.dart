// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AComboField<T> extends StatefulWidget {
  //Criado via controller
  final List<KeyLabel<T>> options;
  final AComboController<T>? fieldController;

  //Parâmetros para criar o controller aqui no TextField
  final String? name;

  final String? labelText;
  final String? hintText;
  final String? helperText;

  final bool? enabled;
  final List<FieldValidator>? validators;

  final InputDecoration localDecoration;

  final void Function(AComboController<T> controller)? onControllerCreated;

  const AComboField.controller(
    AComboController<T> this.fieldController, {
    required this.options,
    super.key,
    InputDecoration? decoration,
  })  : localDecoration = decoration ?? const InputDecoration(),
        name = null,
        labelText = null,
        hintText = null,
        helperText = null,
        enabled = null,
        validators = null,
        onControllerCreated = null;

  const AComboField(
    String this.name, {
    super.key,
    required this.options,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.validators,
    this.onControllerCreated,
    InputDecoration? decoration,
  })  : localDecoration = decoration ?? const InputDecoration(),
        fieldController = null;

  @override
  State<AComboField<T>> createState() => _AComboFieldState<T>();
}

class _AComboFieldState<T> extends State<AComboField<T>> with FieldControllerRegisterMixin {
  late final AComboController<T> fieldController;

  @override
  void initState() {
    super.initState();
    fieldController = registerFormField(context, widget.fieldController, widget.name, _buildController, widget.onControllerCreated);
  }

  @override
  Widget build(BuildContext context) {
    return AConsumer(
      fieldController,
      builder: (context, fieldController) {
        final enabled = fieldController.enabled ?? true;
        return DropdownButtonFormField<T>(
          decoration: widget.localDecoration.copyWith(
            floatingLabelBehavior: FloatingLabelBehavior.always,
            errorText: fieldController.errorText,
            errorMaxLines: 2,
            labelText: widget.localDecoration.labelText ?? fieldController.labelText,
            hintText: widget.localDecoration.hintText ?? fieldController.hintText,
            helperText: widget.localDecoration.helperText ?? fieldController.helperText,
            suffixIcon: _buildSuffixIcon(fieldController),
          ),
          items: widget.options
              .map(
                (item) => DropdownMenuItem<T>(
                  value: item.jsonKey as T?,
                  child: Text(item.toString()),
                ),
              )
              .toList(),
          focusNode: fieldController.focusNode,
          value: fieldController.value,
          onChanged: enabled
              ? (v) {
                  fieldController.value = v as T;
                  fieldController.onChanged();
                }
              : null,
        );
      },
    );
  }

  Widget? _buildSuffixIcon(AComboController<T> fieldController) {
    if (widget.options[0].jsonKey == null && fieldController.value != null) {
      return IconButton(
        icon: const Icon(Icons.clear, color: Colors.red),
        onPressed: () {
          fieldController.value = null as T;
          fieldController.onChanged();
        },
      );
    }
    return null;
  }

  AComboController<T> _buildController() {
    return AComboController<T>(
      widget.name!,
      initialValue: widget.options.first.jsonKey as T?,
      defaultValue: widget.options.first.jsonKey as T,
      labelText: widget.labelText,
      hintText: widget.hintText,
      helperText: widget.helperText,
      enabled: widget.enabled,
      validators: widget.validators,
    );
  }
}
