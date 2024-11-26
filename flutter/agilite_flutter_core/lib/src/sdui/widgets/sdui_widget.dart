import 'package:agilite_flutter_core/src/sdui/sdui_context.dart';
import 'package:flutter/material.dart';

abstract class SduiWidget<T extends SduiModel> {
  T json2Model(Map<String, dynamic> json);
  Widget render(
    BuildContext context,
    SduiContext sduiContext,
    T model,
  );

  Key? buildKey(String? id) {
    if (id == null) return null;
    return ValueKey(id);
  }
}

abstract class SduiModel {
  final String? id;
  const SduiModel({
    this.id,
  });
}
