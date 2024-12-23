import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

typedef AddContollerEvent = void Function(FieldController<dynamic> controller);

class FormController {
  final Map<String, dynamic> _value = {};

  final List<AddContollerEvent> addControllerListeners = [];

  final List<FieldController<dynamic>> _fieldControllers = [];
  List<FieldController<dynamic>> get fieldControllers => _fieldControllers;

  //Panel validations
  final ValueNotifier<List<String>> $validationMessages = ValueNotifier([]);
  GlobalKey? panelValidationGlobalKey;

  T addController<T extends FieldController<dynamic>>(T controller) {
    if (containsControllerByName(controller.name)) {
      throw 'Já existe um controller com a key ${controller.name} no form';
    }

    _fieldControllers.add(controller);
    _mapControllers[controller.name] = controller;
    controller.addedToFormController = true;

    if (_value.containsKey(controller.name)) {
      controller.fillFromJson(_value);
    }

    for (var addControllerListener in addControllerListeners) {
      addControllerListener(controller);
    }
    return controller;
  }

  //---- Map Controller ----
  final Map<String, FieldController<dynamic>> _mapControllers = {};
  final Map<String, dynamic> _customValues = {};

  bool containsControllerByName(String name) {
    return _mapControllers.containsKey(name);
  }

  FieldController<dynamic>? getControllerByName(String name) {
    return _mapControllers[name];
  }

  bool validate() {
    bool isValid = true;
    final msgs = <String>[];
    for (var field in _fieldControllers) {
      final msg = field.validate();
      if (msg != null) {
        msgs.add(msg);
        if (isValid) {
          field.requestFocus();
        }

        isValid = false;
      }
    }
    $validationMessages.value = msgs;
    if (panelValidationGlobalKey?.currentContext != null) {
      Scrollable.ensureVisible(panelValidationGlobalKey!.currentContext!, duration: const Duration(milliseconds: 200));
    }

    return isValid;
  }

  void addValidationMessage(String message) {
    $validationMessages.value = [...$validationMessages.value, message];
  }

  bool isClean() {
    for (var field in _fieldControllers) {
      if (field.status != FieldStatus.clean) return false;
    }
    return true;
  }

  Map<String, dynamic> get controllersValue {
    final Map<String, dynamic> map = {};
    for (var field in _fieldControllers) {
      if (field.jsonValue != null) map[field.name] = field.jsonValue;
    }
    return map;
  }

  Map<String, dynamic> buidlJson([Map<String, dynamic> defaultValues = const {}]) {
    return {..._customValues, ...defaultValues, ...controllersValue};
  }

  bool isChanged(Map<String, dynamic> newValue) {
    return !ObjectUtils.isEquals(controllersValue, newValue);
  }

  set value(Map<String, dynamic>? data) {
    _value.clear();
    _value.addAll(data ?? {});

    for (var field in _fieldControllers) {
      field.fillFromJson(_value);
    }
  }

  void showValues(Map<String, dynamic> data) {
    _value.addAll(data);

    for (var key in data.keys) {
      final field = getControllerByName(key);
      if (field != null) {
        field.fillFromJson(_value);
      }
    }
  }

  void clear() {
    _value.clear();
    $validationMessages.value = [];
    _customValues.clear();
    for (var f in _fieldControllers) {
      f.clear();
    }
  }

  void focusFirst() {
    if (fieldControllers.isEmpty) return;
    final fieldsEnable = fieldControllers.where((element) => element.enabled != false);
    if (fieldsEnable.isEmpty) return;
    final firstAutoFocus = fieldsEnable.where((element) => element.autoFocus).firstOrNull;

    if (firstAutoFocus != null) {
      firstAutoFocus.requestFocus();
    } else {
      fieldsEnable.first.requestFocus();
    }
  }

  void dispose() {
    for (var element in _fieldControllers) {
      element.dispose();
    }
    _fieldControllers.clear();
    $validationMessages.value = [];
  }

  // ---- Register values ----
  void setCustomValue(String key, dynamic value) {
    _customValues[key] = value;
  }

  void removeCustomValue(String key) {
    _customValues.remove(key);
  }

  // ---- Utils to FieldController ----
  void setControllerValue(String controllerName, dynamic value) {
    final controller = getControllerByName(controllerName);
    if (controller == null) {
      throw 'Controller $controllerName não encontrado';
    }
    controller.value = value;
  }

  dynamic getControllerValue(String controllerName) {
    final controller = getControllerByName(controllerName);
    if (controller == null) {
      throw 'Controller $controllerName não encontrado';
    }
    return controller.value;
  }
}
