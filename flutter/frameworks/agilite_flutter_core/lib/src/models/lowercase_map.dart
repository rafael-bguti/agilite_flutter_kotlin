import 'dart:collection';

class LowercaseMap extends MapBase<String, dynamic> {
  final Map<String, dynamic> _innerMap = {};

  LowercaseMap();

  factory LowercaseMap.fromMap(Map<String, dynamic>? other) {
    if (other == null) return LowercaseMap();
    if (other is LowercaseMap) return other;

    final map = LowercaseMap();
    map._convertAll(other);
    return map;
  }

  @override
  dynamic operator [](Object? key) {
    if (key is String) {
      var lowerCaseKey = key.toLowerCase();
      return _innerMap[lowerCaseKey];
    }
    return null;
  }

  @override
  void operator []=(String key, dynamic value) {
    var lowerCaseKey = key.toLowerCase();
    _innerMap[lowerCaseKey] = value;
  }

  @override
  void clear() {
    _innerMap.clear();
  }

  @override
  Iterable<String> get keys => _innerMap.keys;

  @override
  dynamic remove(Object? key) {
    if (key is String) {
      var lowerCaseKey = key.toLowerCase();
      return _innerMap.remove(lowerCaseKey);
    }
    return null;
  }

  void _convertAll(Map<String, dynamic> other) {
    other.forEach((key, value) {
      if (value is Map<String, dynamic>) value = LowercaseMap.fromMap(value);
      this[key] = value;
    });
  }

  //---- Get Values -----
  double? getDouble(String key, [double? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    if (value is double) return value;

    this[key] = value is num ? value.toDouble() : double.parse(value.toString());
    return this[key] as double;
  }

  int? getInt(String key, [int? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    if (value is int) return value;

    this[key] = value is num ? value.toInt() : int.parse(value.toString());
    return this[key] as int;
  }

  bool? getBool(String key, [bool? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    if (value is bool) return value;

    this[key] = bool.parse(value.toString());
    return this[key] as bool;
  }

  String? getString(String key, [String? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    return value.toString();
  }

  DateTime? getDateTime(String key, [DateTime? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    if (value is DateTime) return value;

    this[key] = DateTime.parse(value.toString());
    return this[key] as DateTime;
  }
}
