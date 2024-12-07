import 'package:agilite_flutter_core/core.dart';

class ObjectUtils {
  static bool isEquals(dynamic a, dynamic b) {
    if (a == null && b == null) return true;
    if (a == null || b == null) return false;

    if (a is List) {
      if (b is! List) return false;
      return a.isEquals(b);
    } else if (a is Map) {
      if (b is! Map) return false;
      return a.isEquals(b);
    } else if (a is Iterable) {
      if (b is! Iterable) return false;
      return a.toList().isEquals((b).toList());
    }

    if (a.runtimeType != b.runtimeType) return false;

    return a == b;
  }
}
