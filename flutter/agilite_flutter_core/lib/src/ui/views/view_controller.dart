import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ViewController<T> extends ChangeNotifier {
  ViewController([T? initialState]) : _state = initialState;

  void onViewLoaded() {}

  T? _state;
  bool get hasState => _state != null;
  T get state => _state!;
  set state(T t) {
    _state = t;
    showState();
  }

  void showState() {
    closeError();
    closeLoading();

    notifyListeners();
  }

  @override
  void dispose() {
    print('DISPOSE $runtimeType');
    super.dispose();
  }
}
