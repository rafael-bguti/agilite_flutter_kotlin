import 'dart:async';

import 'package:flutter/material.dart';

class DebounceTimer {
  final Duration duration;
  Timer? _timer;

  DebounceTimer([this.duration = const Duration(milliseconds: 500)]);

  void run(VoidCallback action) {
    if (_timer != null) {
      _timer!.cancel();
    }
    _timer = Timer(duration, action);
  }

  void dispose() {
    _timer?.cancel();
  }
}
