import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'field_controller.dart';

abstract class FormFieldController<T> extends FieldController<T> {
  final TextEditingController textEditingController;

  String? _helperText;
  String? _hintText;

  bool? _obscureText;
  bool? _showMaxLenght;

  int? _maxLines;
  int? _maxLength;

  TextInputType? _keyboardType;
  TextInputAction? _textInputAction;

  bool _isControllerProvided;

  FormFieldController(
    super.name, {
    required super.defaultValue,
    super.initialValue,
    super.enabled,
    super.validators,
    super.labelText,
    super.req,
    super.autoFocus,
    String? helperText,
    String? hintText,
    bool? obscureText,
    int? maxLines,
    int? maxLength,
    TextEditingController? textEditingController,
    TextInputType? keyboardType,
    TextInputAction? textInputAction,
    super.createdBySpread,
  })  : _isControllerProvided = textEditingController != null,
        textEditingController = textEditingController ?? TextEditingController(),
        _helperText = helperText,
        _hintText = hintText,
        _obscureText = obscureText,
        _maxLines = maxLines,
        _maxLength = maxLength {
    _init();
  }

  void _init() {
    textEditingController.addListener(() {
      if (status != FieldStatus.clean) {
        validate();
      }
    });

    focusNode.addListener(() {
      if (focusNode.hasFocus) {
        _selectAllText();
      } else {
        status = FieldStatus.dirty;
        validate();
      }
    });
  }

  void _selectAllText() {
    if (createdBySpread) return;
    if (textEditingController.text.isNotEmpty) {
      textEditingController.selection = TextSelection(baseOffset: 0, extentOffset: textEditingController.text.length);
    }
  }

  //---- Methods to be replaced by children ----
  T parse(String text);
  String format(T? value);

  TextAlign? get textAlign => null;
  List<TextInputFormatter>? get inputFormatters => null;
  VoidCallback? suffixStartAction;
  VoidCallback? suffixCompleteAction;
  Widget? suffixIcon(BuildContext context, bool enabled) => null;

  //---- Overrides ----
  @override
  get value => parse(textEditingController.text) ?? defaultValue;

  @override
  set value(value) {
    final localText = format(value);
    if (localText == textEditingController.text) return;

    textEditingController.text = localText;
    status = FieldStatus.dirty;
    validate();
    notifyListeners();
  }

  @override
  void dispose() {
    if (!_isControllerProvided) textEditingController.dispose();
    super.dispose();
  }

  //---- Setters e Getters ----
  @protected
  String get text => textEditingController.text;

  String? get helperText => _helperText;
  set helperText(String? helperText) {
    if (helperText != _helperText) {
      _helperText = helperText;
      notifyListeners();
    }
  }

  String? get hintText => _hintText;
  set hintText(String? hintText) {
    if (hintText != _hintText) {
      _hintText = hintText;
      notifyListeners();
    }
  }

  bool? get obscureText => _obscureText;
  set obscureText(bool? obscureText) {
    if (obscureText != _obscureText) {
      _obscureText = obscureText;
      notifyListeners();
    }
  }

  int? get maxLines => _maxLines;
  set maxLines(int? maxLines) {
    if (maxLines != _maxLines) {
      _maxLines = maxLines;
      notifyListeners();
    }
  }

  int? get maxLength => _maxLength;
  set maxLength(int? maxLength) {
    if (maxLength != _maxLength) {
      _maxLength = maxLength;
      notifyListeners();
    }
  }

  bool? get showMaxLenght => _showMaxLenght;
  set showMaxLenght(bool? showMaxLenght) {
    if (showMaxLenght != _showMaxLenght) {
      _showMaxLenght = showMaxLenght;
      notifyListeners();
    }
  }

  TextInputType? get keyboardType => _keyboardType;
  set keyboardType(TextInputType? keyboardType) {
    if (keyboardType != _keyboardType) {
      _keyboardType = keyboardType;
      notifyListeners();
    }
  }

  TextInputAction? get textInputAction => _textInputAction;
  set textInputAction(TextInputAction? textInputAction) {
    if (textInputAction != _textInputAction) {
      _textInputAction = textInputAction;
      notifyListeners();
    }
  }
}
