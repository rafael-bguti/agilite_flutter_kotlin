import 'package:flutter/material.dart';

extension ObservableExtension<T> on T {
  ValueNotifier<T> get obs => ValueNotifier<T>(this);
}
