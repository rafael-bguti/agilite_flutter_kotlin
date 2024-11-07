import 'dart:async';

import 'package:flutter/material.dart';

void runOnNextBuild(VoidCallback function) {
  scheduleMicrotask(function);
}
