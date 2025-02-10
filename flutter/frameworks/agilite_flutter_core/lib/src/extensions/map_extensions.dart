import 'package:agilite_flutter_core/core.dart';

extension MapExtension on Map<dynamic, dynamic> {
  bool isEquals(Map<dynamic, dynamic> other) {
    if (length != other.length) return false;
    for (var key in keys) {
      if (!ObjectUtils.isEquals(this[key], other[key])) return false;
    }

    return true;
  }

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

  Map<String, dynamic>? getMap(String key) {
    var value = this[key];
    if (value == null) return null;
    if (value is Map<String, dynamic>) return value;

    this[key] = value.toString().toMap();
    return this[key] as Map<String, dynamic>;
  }

  DateTime? getDateTime(String key, [DateTime? defaultValue]) {
    var value = this[key];
    if (value == null) return defaultValue;
    if (value is DateTime) return value;

    this[key] = DateTime.parse(value.toString());
    return this[key] as DateTime;
  }
}
