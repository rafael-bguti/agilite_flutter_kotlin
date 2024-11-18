import 'package:agilite_flutter_core/core.dart';

abstract class KeyLabel<T> {
  dynamic get jsonKey;
  String get label;
  LowercaseMap? get allData;

  @override
  String toString() => label;

  Map<String, dynamic>? getMoreDetails();
}

class LocalKeyLabel<T> extends KeyLabel<T> {
  final T? key;
  @override
  final String label;
  final Map<String, dynamic>? moreDetails;

  LocalKeyLabel(
    this.key,
    this.label, {
    this.moreDetails,
  });

  LocalKeyLabel.keyOnly(this.key)
      : label = key.toString(),
        moreDetails = null;

  @override
  bool operator ==(covariant LocalKeyLabel<T> other) {
    return other.key == key;
  }

  @override
  LowercaseMap? get allData => null;

  @override
  int get hashCode => key.hashCode;

  @override
  dynamic get jsonKey => key;

  @override
  Map<String, dynamic>? getMoreDetails() => moreDetails;
}

class RemoteKeyLabel<T> extends KeyLabel<T> {
  final T key;
  @override
  final String label;
  @override
  final LowercaseMap allData;

  RemoteKeyLabel(this.key, this.label, this.allData);

  @override
  bool operator ==(covariant RemoteKeyLabel<T> other) {
    return other.key == key;
  }

  factory RemoteKeyLabel.fromMap(Map<String, dynamic> map) {
    return RemoteKeyLabel<T>(
      map['key'] as T,
      map['label'] as String,
      LowercaseMap.fromMap(map['allData'] as Map<String, dynamic>),
    );
  }

  @override
  int get hashCode => key.hashCode;

  @override
  dynamic get jsonKey => allData;

  @override
  Map<String, dynamic>? getMoreDetails() => allData;
}
