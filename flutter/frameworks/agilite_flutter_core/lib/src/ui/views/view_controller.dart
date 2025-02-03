import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ViewController<T> extends ChangeNotifier {
  ViewController([T? initialState]) : _state = initialState;

  Future<void> onViewLoaded() async {}

  T? _state;
  bool get hasState => _state != null;
  T get state => _state!;
  set state(T t) {
    _state = t;
    showState();
  }

  set stateDontCloseLoading(T t) {
    _state = t;
    showState(false);
  }

  void showState([bool hideLoading_ = true]) {
    hideError();
    if (hideLoading_) hideLoading();

    notifyListeners();
  }

  @override
  void dispose() {
    super.dispose();
  }
}
