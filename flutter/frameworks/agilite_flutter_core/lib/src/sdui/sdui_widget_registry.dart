import 'package:agilite_flutter_core/src/sdui/widgets/sdui_crud.dart';

import 'widgets/widgets.dart';

class SduiWidgetRegistry {
  static final SduiWidgetRegistry instance = SduiWidgetRegistry._();
  SduiWidgetRegistry._() {
    putAll(_initWidgets);
  }

  final Map<String, SduiWidget> _registry = {};

  void putAll(List<SduiWidget> components) {
    for (final component in components) {
      _registry[component.runtimeType.toString()] = component;
    }
  }

  void put(SduiWidget component) {
    _registry[component.runtimeType.toString()] = component;
  }

  SduiWidget get(String name) {
    final component = _registry[name];

    if (component == null) {
      throw 'SDUI-Component "$name" not found';
    }
    return component;
  }
}

List<SduiWidget> _initWidgets = [
  SduiCrud(),
  SduiText(),
  SduiColumn(),
];
