import 'package:agilite_flutter_core/core.dart';

extension MapExtension on Map<dynamic, dynamic> {
  bool isEquals(Map<dynamic, dynamic> other) {
    if (length != other.length) return false;
    for (var key in keys) {
      if (!ObjectUtils.isEquals(this[key], other[key])) return false;
    }

    return true;
  }
}
