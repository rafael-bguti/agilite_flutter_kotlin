import 'package:hive_flutter/hive_flutter.dart';

abstract class Storage {
  Future<String?> get(String key);
  Future<String> getOrDefault(String key, String defaultValue);
  Future<void> put(String key, String? value);
  Future<void> delete(String key);
  Future<void> clear();
}

class HiveStorage extends Storage {
  final String boxName;

  HiveStorage(this.boxName);

  @override
  Future<String?> get(String key) async {
    final box = await _getBox();
    return box.get(key);
  }

  @override
  Future<String> getOrDefault(String key, String defaultValue) async {
    final box = await _getBox();
    return box.get(key) ?? defaultValue;
  }

  @override
  Future<void> put(String key, String? value) async {
    final box = await _getBox();
    await box.put(key, value);
  }

  @override
  Future<void> delete(String key) async {
    final box = await _getBox();
    await box.delete(key);
  }

  @override
  Future<void> clear() async {
    final box = await _getBox();
    await box.clear();
  }

  Box<String?>? _box;
  Future<Box<String?>> _getBox() async {
    if (_box != null) return _box!;
    await Hive.initFlutter();
    _box = await Hive.openBox(boxName);

    return _box!;
  }
}
