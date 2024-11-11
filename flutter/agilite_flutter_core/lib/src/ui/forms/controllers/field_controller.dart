import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/widgets.dart';

enum FieldStatus {
  clean,
  dirty,
  error,
  valid,
}

abstract class FieldController<T> extends ChangeNotifier {
  final FocusNode focusNode = FocusNode();

  final String name;
  FieldStatus status;

  String? _labelText;
  bool? _enabled;

  String? _errorText;
  List<FieldValidator>? validators;

  T defaultValue;
  ChangeNotifier onValueChanged = ChangeNotifier();

  bool addedToFormController = false;
  bool req;
  bool autoFocus;

  bool createdBySpread;

  FieldController(
    this.name, {
    required this.defaultValue,
    this.validators,
    this.req = false,
    this.status = FieldStatus.clean,
    this.createdBySpread = false,
    this.autoFocus = false,
    T? initialValue,
    String? labelText,
    bool? enabled,
  })  : _enabled = enabled,
        _labelText = labelText {
    value = initialValue ?? defaultValue;
    this.status = FieldStatus.clean;
  }

  String? validate() {
    _errorText = _doValidate();
    FieldStatus localStatus = _errorText == null ? FieldStatus.valid : FieldStatus.error;
    if (this.status != localStatus) {
      this.status = localStatus;
      notifyListeners();
    }
    return _errorText != null ? '$labelText: $_errorText' : null;
  }

  void clear() {
    value = defaultValue;
    _errorText = null;
    status = FieldStatus.clean;
    notifyListeners();
  }

  void clearError() {
    _errorText = null;
    notifyListeners();
  }

  void onChanged() {
    onValueChanged.notifyListeners();
  }

  String? get errorText => _errorText;
  void addValidationError(String errorText) {
    _errorText = errorText;
    notifyListeners();
  }

  //---- Methods to be replaced by children ----
  void requestFocus() {
    focusNode.requestFocus();
  }

  T get value;
  set value(T value);

  get jsonValue;
  void fillFromJson(Map<String, dynamic>? data);

  //---- Setters e Getters ----
  bool? get enabled => _enabled;
  set enabled(bool? enabled) {
    if (enabled != _enabled) {
      _enabled = enabled;
      notifyListeners();
    }
  }

  String? get labelText => _labelText;
  set labelText(String? labelText) {
    if (labelText != _labelText) {
      _labelText = labelText;
      notifyListeners();
    }
  }

  @override
  void dispose() {
    focusNode.dispose();
    super.dispose();
  }

  String? _doValidate() {
    if (!validators.isNullOrEmpty) {
      for (final validation in validators!) {
        final error = validation(value);
        if (error != null) {
          return error;
        }
      }
    }

    if (req) {
      return Validations.isNotEmpty(value);
    }

    return null;
  }
}
