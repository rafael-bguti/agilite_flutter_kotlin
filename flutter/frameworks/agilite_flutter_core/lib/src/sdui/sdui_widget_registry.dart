import 'package:agilite_flutter_core/src/sdui/widgets/sdui_crud.dart';
import 'package:agilite_flutter_core/src/sdui/widgets/sdui_fieldset.dart';
import 'package:agilite_flutter_core/src/sdui/widgets/sdui_padding.dart';

import 'widgets/sdui_autocomplete.dart';
import 'widgets/sdui_combo_field.dart';
import 'widgets/sdui_divider.dart';
import 'widgets/sdui_grid.dart';
import 'widgets/sdui_metadata_field.dart';
import 'widgets/sdui_sized_box.dart';
import 'widgets/sdui_spacing_column.dart';
import 'widgets/sdui_text.dart';
import 'widgets/sdui_widget.dart';

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
  SduiAutocomplete(),
  SduiComboField(),
  SduiCrud(),
  SduiDivider(),
  SduiFieldset(),
  SduiGrid(),
  SduiMetadataField(),
  SduiPadding(),
  SduiSizedBox(),
  SduiSpacingColumn(),
  SduiText(),
];
