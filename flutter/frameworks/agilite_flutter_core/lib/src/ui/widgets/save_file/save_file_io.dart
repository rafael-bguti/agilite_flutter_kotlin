import 'dart:io';
import 'dart:typed_data';

import 'package:file_picker/file_picker.dart';

Future<String?> saveFile(String fileName, Uint8List bytes) async {
  String? selectedDir = await FilePicker.platform.getDirectoryPath();
  if (selectedDir == null) {
    return null;
  }

  final path = '$selectedDir/$fileName';
  final file = createNewFile(path);

  file.writeAsBytesSync(bytes);
  return file.path;
}

File createNewFile(String path) {
  var file = File(path);
  if (file.existsSync()) {
    final ext = path.split('.').last;
    path = path.replaceAll('.$ext', '');

    int i = 1;
    while (file.existsSync()) {
      file = File('$path ($i).$ext');
      i++;
    }
  }
  return file;
}
