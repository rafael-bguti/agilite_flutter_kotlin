import 'package:agilite_flutter_core/core.dart';

class ComboController<T> extends FieldController<T> {
  String? _helperText;
  String? _hintText;

  ComboController(
    super.name, {
    required super.defaultValue,
    super.initialValue,
    super.enabled,
    super.labelText,
    String? helperText,
    String? hintText,
    super.validators,
    super.createdBySpreadColumn,
  })  : _helperText = helperText,
        _hintText = hintText,
        _value = defaultValue;

  T _value;

  //---- Overrides ----
  @override
  get value => _value;

  @override
  set value(T value) {
    if (value == _value) return;

    _value = value ?? defaultValue;

    onValueChanged();
    notifyListeners();
  }

  @override
  get jsonValue => value;

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    value = data?[name] as T ?? defaultValue;
  }

  //---- Setters e Getters ----
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
}
