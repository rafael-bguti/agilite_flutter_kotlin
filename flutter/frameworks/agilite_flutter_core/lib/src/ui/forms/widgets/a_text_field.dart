// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

enum FieldType {
  string,
  date,
  int,
  double,
}

class ATextField extends StatefulWidget {
  //Criado via controller
  final FormFieldController<dynamic>? fieldController;

  //Parâmetros para criar o controller aqui no TextField
  final String? name;
  final bool req;

  final dynamic initialValue;
  final dynamic defaultValue;

  final String? labelText;
  final String? hintText;
  final String? helperText;

  final bool? enabled;
  final int? maxLines;
  final int? maxLength;
  final bool? showMaxLength;
  final bool? obscureText;
  final bool? autoFocus;

  final List<TextInputFormatter>? inputFormatters;
  final List<FieldValidator>? validators;

  final TextInputType? keyboardType;
  final TextInputAction? textInputAction;
  final TextEditingController? textEditingController;

  final InputDecoration localDecoration;
  final FieldType? _fieldType;

  final int? maxIntegerDigits;
  final int? maxDecimalDigits;
  final int? minDecimalDigits;

  final void Function(FormFieldController<dynamic> controller)? onControllerCreated;
  final void Function(String? value)? onChanged;

  const ATextField.controller(
    FormFieldController<dynamic> this.fieldController, {
    super.key,
    InputDecoration? decoration,
    this.req = false,
    this.onChanged,
  })  : _fieldType = null,
        localDecoration = decoration ?? const InputDecoration(),
        name = null,
        initialValue = null,
        defaultValue = null,
        labelText = null,
        hintText = null,
        helperText = null,
        enabled = null,
        maxLines = null,
        maxLength = null,
        showMaxLength = null,
        obscureText = null,
        inputFormatters = null,
        keyboardType = null,
        textInputAction = null,
        textEditingController = null,
        maxDecimalDigits = null,
        maxIntegerDigits = null,
        minDecimalDigits = null,
        validators = null,
        autoFocus = null,
        onControllerCreated = null;

  const ATextField.string(
    String this.name, {
    super.key,
    this.req = false,
    this.initialValue,
    this.defaultValue,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.maxLines,
    this.maxLength,
    this.showMaxLength,
    this.obscureText,
    this.inputFormatters,
    this.keyboardType,
    this.textInputAction,
    this.textEditingController,
    this.validators,
    this.autoFocus,
    this.onControllerCreated,
    this.onChanged,
    InputDecoration? decoration,
  })  : _fieldType = FieldType.string,
        localDecoration = decoration ?? const InputDecoration(),
        fieldController = null,
        maxDecimalDigits = null,
        maxIntegerDigits = null,
        minDecimalDigits = null;

  const ATextField.double(
    String this.name, {
    this.req = false,
    this.initialValue,
    this.defaultValue,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.maxLines,
    this.maxLength,
    this.showMaxLength,
    this.obscureText,
    this.inputFormatters,
    this.keyboardType,
    this.textInputAction,
    this.textEditingController,
    this.maxIntegerDigits,
    this.maxDecimalDigits,
    this.minDecimalDigits,
    this.validators,
    this.autoFocus,
    this.onControllerCreated,
    this.onChanged,
    InputDecoration? decoration,
    super.key,
  })  : _fieldType = FieldType.double,
        localDecoration = decoration ?? const InputDecoration(),
        fieldController = null;

  const ATextField.int(
    String this.name, {
    this.req = false,
    this.initialValue,
    this.defaultValue,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.maxLines,
    this.maxLength,
    this.showMaxLength,
    this.obscureText,
    this.inputFormatters,
    this.keyboardType,
    this.textInputAction,
    this.textEditingController,
    this.maxIntegerDigits,
    this.validators,
    this.autoFocus,
    this.onControllerCreated,
    this.onChanged,
    InputDecoration? decoration,
    super.key,
  })  : _fieldType = FieldType.int,
        localDecoration = decoration ?? const InputDecoration(),
        fieldController = null,
        maxDecimalDigits = null,
        minDecimalDigits = null;

  const ATextField.date(
    String this.name, {
    this.req = false,
    this.initialValue,
    this.defaultValue,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.maxLines,
    this.maxLength,
    this.showMaxLength,
    this.obscureText,
    this.inputFormatters,
    this.keyboardType,
    this.textInputAction,
    this.textEditingController,
    this.validators,
    this.autoFocus,
    this.onControllerCreated,
    this.onChanged,
    InputDecoration? decoration,
    super.key,
  })  : _fieldType = FieldType.date,
        localDecoration = decoration ?? const InputDecoration(),
        fieldController = null,
        maxDecimalDigits = null,
        maxIntegerDigits = null,
        minDecimalDigits = null;

  const ATextField.all(
    FieldType this._fieldType,
    String this.name, {
    super.key,
    this.req = false,
    this.initialValue,
    this.defaultValue,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.maxLines,
    this.maxLength,
    this.showMaxLength,
    this.obscureText,
    this.inputFormatters,
    this.keyboardType,
    this.textInputAction,
    this.textEditingController,
    this.validators,
    this.autoFocus,
    this.maxIntegerDigits,
    this.maxDecimalDigits,
    this.minDecimalDigits,
    this.onControllerCreated,
    this.onChanged,
    InputDecoration? decoration,
  })  : localDecoration = decoration ?? const InputDecoration(),
        fieldController = null;

  @override
  State<ATextField> createState() => _ATextFieldState();
}

class _ATextFieldState extends State<ATextField> with FieldControllerCreatorMixin {
  late final FormFieldController<dynamic> fieldController;

  @override
  void initState() {
    super.initState();
    fieldController = createFieldController(context, widget.fieldController, widget.name, _buildController, widget.onControllerCreated);
  }

  @override
  Widget build(BuildContext context) {
    return AConsumer(
        notifier: fieldController,
        builder: (context, fieldController, _) {
          final enabled = fieldController.enabled ?? true;
          return TextFormField(
            decoration: widget.localDecoration.copyWith(
              floatingLabelBehavior: FloatingLabelBehavior.always,
              errorText: fieldController.errorText,
              errorMaxLines: 2,
              suffixIcon: fieldController.suffixIcon(context, enabled),
              labelText: widget.localDecoration.labelText ?? fieldController.labelText,
              hintText: widget.localDecoration.hintText ?? fieldController.hintText,
              hintStyle: widget.localDecoration.hintStyle ?? const TextStyle(color: Color(0xFFBABABA)), //TODO: DarkMode - ver cor para o dark mode
              helperText: widget.localDecoration.helperText ?? fieldController.helperText,
              counterText: (widget.showMaxLength ?? false) ? null : "",
            ),
            textAlignVertical: TextAlignVertical.center,
            textAlign: fieldController.textAlign ?? TextAlign.start,
            enabled: enabled,
            controller: fieldController.textEditingController,
            focusNode: fieldController.focusNode,
            keyboardType: fieldController.keyboardType,
            inputFormatters: fieldController.inputFormatters,
            maxLines: fieldController.maxLines ?? 1,
            maxLength: fieldController.maxLength,
            textInputAction: fieldController.textInputAction,
            obscureText: fieldController.obscureText ?? false,
            onChanged: (v) {
              widget.onChanged?.call(v);
              fieldController.onChanged();
            },
            autofocus: fieldController.autoFocus,
          );
        });
  }

  FormFieldController<dynamic> _buildController() {
    final fieldController = switch (widget._fieldType!) {
      FieldType.string => StringController(
          widget.name!,
          initialValue: widget.initialValue as String?,
          defaultValue: widget.defaultValue as String?,
          labelText: widget.labelText,
          hintText: widget.hintText,
          helperText: widget.helperText,
          enabled: widget.enabled,
          maxLines: widget.maxLines,
          maxLength: widget.maxLength,
          obscureText: widget.obscureText,
          keyboardType: widget.keyboardType,
          textInputAction: widget.textInputAction,
          textEditingController: widget.textEditingController,
          inputFormatters: widget.inputFormatters,
          validators: widget.validators,
          req: widget.req,
          autoFocus: widget.autoFocus ?? false,
        ),
      FieldType.date => DateController(
          widget.name!,
          initialValue: widget.initialValue as DateTime?,
          defaultValue: widget.defaultValue as DateTime?,
          labelText: widget.labelText,
          hintText: widget.hintText,
          helperText: widget.helperText,
          enabled: widget.enabled,
          maxLines: widget.maxLines,
          maxLength: widget.maxLength,
          obscureText: widget.obscureText,
          keyboardType: widget.keyboardType,
          textInputAction: widget.textInputAction,
          textEditingController: widget.textEditingController,
          validators: widget.validators,
          req: widget.req,
          autoFocus: widget.autoFocus ?? false,
        ),
      FieldType.int => IntController(
          widget.name!,
          initialValue: widget.initialValue as int?,
          defaultValue: widget.defaultValue as int?,
          labelText: widget.labelText,
          hintText: widget.hintText,
          helperText: widget.helperText,
          enabled: widget.enabled,
          maxLines: widget.maxLines,
          obscureText: widget.obscureText,
          keyboardType: widget.keyboardType,
          textInputAction: widget.textInputAction,
          textEditingController: widget.textEditingController,
          maxIntegerDigits: widget.maxIntegerDigits ?? 9,
          validators: widget.validators,
          req: widget.req,
          autoFocus: widget.autoFocus ?? false,
        ),
      FieldType.double => DoubleController(
          widget.name!,
          initialValue: widget.initialValue as double?,
          defaultValue: widget.defaultValue as double?,
          labelText: widget.labelText,
          hintText: widget.hintText,
          helperText: widget.helperText,
          enabled: widget.enabled,
          maxLines: widget.maxLines,
          obscureText: widget.obscureText,
          keyboardType: widget.keyboardType,
          textInputAction: widget.textInputAction,
          textEditingController: widget.textEditingController,
          maxIntegerDigits: widget.maxIntegerDigits ?? 9,
          maxDecimalDigits: widget.maxDecimalDigits ?? 2,
          minDecimalDigits: widget.minDecimalDigits ?? 2,
          validators: widget.validators,
          req: widget.req,
          autoFocus: widget.autoFocus ?? false,
        )
    } as FormFieldController;

    return fieldController;
  }
}
