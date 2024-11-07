import 'dart:convert';

import '../models/lowercase_map.dart';

extension JsonExtensions on String? {
  Map<String, dynamic> toMap() {
    if (this == null || this!.trim().isEmpty) return {};
    return json.decode(this!) as Map<String, dynamic>;
  }

  LowercaseMap toLowercaseMap() {
    if (this == null || this!.trim().isEmpty) return LowercaseMap();
    return LowercaseMap.fromMap(json.decode(this!) as Map<String, dynamic>);
  }

  List<Map<String, dynamic>> toListMap() {
    if (this == null || this!.trim().isEmpty) return [];
    return List<Map<String, dynamic>>.from(json.decode(this!) as List).toList();
  }

  List<LowercaseMap> toListLowercaseMap() {
    if (this == null || this!.trim().isEmpty) return <LowercaseMap>[];
    return List<Map<String, dynamic>>.from(json.decode(this!) as List)
        .map(
          (e) => LowercaseMap.fromMap(e),
        )
        .toList();
  }
}
