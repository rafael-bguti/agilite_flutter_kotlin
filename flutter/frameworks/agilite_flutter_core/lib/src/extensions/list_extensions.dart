import 'package:agilite_flutter_core/core.dart';

extension NullableListExtension<T> on List<T>? {
  bool get isNullOrEmpty => this == null || this!.isEmpty;
}

extension ListExtension<T> on List<T> {
  /// Partitions a list into a [count] lists
  List<List<T>> partitionIn(int count) {
    var partitionLength = (length / count).round();
    var partitions = List<List<T>>.generate(count, (i) => sublist(partitionLength * i, (i + 1) * partitionLength <= length ? (i + 1) * partitionLength : null));
    return partitions;
  }

  /// Partitions a list into a list with [maxSize] lists
  List<List<T>> partitionOfMaxSize(int maxSize) {
    assert(maxSize > 0);
    List<List<T>> partitions = [];
    for (int i = 0; i < length; i++) {
      if (i % maxSize == 0) partitions.add([]);
      partitions.last.add(this[i]);
    }
    return partitions;
  }

  T? firstWhereOrNull(bool Function(T element) test) {
    for (var element in this) {
      if (test(element)) return element;
    }
    return null;
  }

  T? get firstOrNull {
    if (isEmpty) return null;
    return first;
  }

  bool isEquals(List<dynamic> other) {
    if (length != other.length) return false;
    for (var i = 0; i < length; i++) {
      if (!ObjectUtils.isEquals(this[i], other[i])) return false;
    }
    return true;
  }
}
