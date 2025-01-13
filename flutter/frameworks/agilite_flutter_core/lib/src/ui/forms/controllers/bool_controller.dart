import 'package:agilite_flutter_core/core.dart';

class BoolController extends FieldController<bool> {
  BoolController(
    super.name, {
    super.defaultValue = false,
    super.initialValue,
    super.enabled,
    super.labelText,
    super.createdBySpreadColumn,
    super.autoFocus,
  }) : _value = defaultValue;

  bool _value;

  //---- Overrides ----
  @override
  get value => _value;

  @override
  set value(bool? value) {
    if (value == _value) return;

    _value = value ?? defaultValue;
    notifyListeners();
  }

  @override
  get jsonValue => value;

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    final json = data?[name];
    if (json is bool) {
      value = json;
    } else if (json is String) {
      value = json == 'true';
    } else {
      value = defaultValue;
    }
  }
}
