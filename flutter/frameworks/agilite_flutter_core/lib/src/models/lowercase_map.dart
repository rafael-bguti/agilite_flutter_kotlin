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
}
