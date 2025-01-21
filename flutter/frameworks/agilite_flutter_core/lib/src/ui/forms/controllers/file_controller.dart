import 'dart:io';

import 'package:agilite_flutter_core/core.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

class FileController extends ChangeNotifier {
  String? _errorText;
  FilePickerResult? result;
  TextEditingController textController = TextEditingController();

  FileController();

  @override
  void dispose() {
    super.dispose();
  }

  void onSelectFile(FilePickerResult? result) {
    this.result = result;
    textController.text = path;

    notifyListeners();
  }

  String get path {
    if (result == null) return '';
    if (PlatformInfo.isDesktop) {
      return result?.files.single.path ?? '';
    } else {
      return result?.files.single.name ?? '';
    }
  }

  Future<Uint8List?> get bytes async {
    if (result == null) return null;
    if (kIsWeb) {
      return result!.files.single.bytes;
    } else {
      final file = File(result!.files.single.path!);
      return await file.readAsBytes();
    }
  }

  void clear() {
    onSelectFile(null);
  }

  String? get errorText => _errorText;
  void addValidationError(String errorText) {
    _errorText = errorText;
    notifyListeners();
  }

  void clearError() {
    _errorText = null;
    notifyListeners();
  }
}
