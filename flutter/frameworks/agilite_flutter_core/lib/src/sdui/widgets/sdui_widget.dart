import 'package:flutter/material.dart';

abstract class SduiWidget<T extends SduiModel> {
  T json2Model(Map<String, dynamic> json);
  Widget render(
    BuildContext context,
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
