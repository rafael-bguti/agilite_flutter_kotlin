import 'package:agilite_flutter_core/src/sdui/sdui_widget_registry.dart';
import 'package:flutter/material.dart';

class SduiRender {
  static Widget renderFromJson(
    BuildContext context,
    Map<String, dynamic> json,
  ) {
    final String name = json["widget"].toString();

    final widget = SduiWidgetRegistry.instance.get(name);
    final model = widget.json2Model(json);

    return widget.render(context, model);
  }
}
