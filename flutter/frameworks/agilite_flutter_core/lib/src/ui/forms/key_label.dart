import 'package:agilite_flutter_core/core.dart';

abstract class Option<T> {
  dynamic get jsonKey;
  String get label;
  LowercaseMap? get allData;

  @override
  String toString() => label;

  Map<String, dynamic>? getMoreDetails();
}

class LocalOption<T> extends Option<T> {
  final T? key;
  @override
  final String label;
  final Map<String, dynamic>? moreDetails;

  LocalOption(
    this.key,
    this.label, {
    this.moreDetails,
  });

  LocalOption.keyOnly(this.key)
      : label = key.toString(),
        moreDetails = null;

  @override
  bool operator ==(covariant LocalOption<T> other) {
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

  factory LocalOption.fromJson(Map<String, dynamic> map) {
    return LocalOption<T>(
      map['key'] as T,
      map['label'] as String,
    );
  }
}

class RemoteOption<T> extends Option<T> {
  final T key;
  @override
  final String label;
  @override
  final LowercaseMap allData;

  RemoteOption(this.key, this.label, this.allData);

  @override
  bool operator ==(covariant RemoteOption<T> other) {
    return other.key == key;
  }

  factory RemoteOption.fromJson(Map<String, dynamic> map) {
    return RemoteOption<T>(
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
