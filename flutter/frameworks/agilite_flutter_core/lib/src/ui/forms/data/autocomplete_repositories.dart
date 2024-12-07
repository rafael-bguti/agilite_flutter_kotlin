import 'dart:async';

import 'package:agilite_flutter_core/core.dart';

abstract class AutocompleteRepository<T> {
  const AutocompleteRepository();

  FutureOr<List<KeyLabel<T>>> findByQuery(String? query);
  KeyLabel<T>? fromValue(dynamic value);
}

class AllInMemoryAutocompleteRepository<T> extends AutocompleteRepository<T> {
  final int pageSize;
  List<KeyLabel<T>> options;

  AllInMemoryAutocompleteRepository(
    this.options, {
    this.pageSize = 50,
  });

  @override
  List<KeyLabel<T>> findByQuery(String? query) {
    final result = query == null ? options : options.where((opt) => opt.toString().toLowerCase().contains(query.toLowerCase())).toList();
    if (result.length > pageSize) {
      return result.sublist(0, pageSize);
    } else {
      return result;
    }
  }

  @override
  KeyLabel<T>? fromValue(dynamic value) {
    return options.firstWhereOrNull((opt) => opt.jsonKey == value);
  }
}

class RemoteAPIAutocompleteRepository<T> extends AutocompleteRepository<T> {
  final HttpProvider http;
  final String autocompleteMetadataName;
  final ClientWhere? Function()? defaultWhereBuilder;

  final FieldMetadata _fieldMetadata;
  RemoteAPIAutocompleteRepository({
    required this.http,
    required this.autocompleteMetadataName,
    this.defaultWhereBuilder,
  }) : _fieldMetadata = metadataRepository.field(autocompleteMetadataName) {
    assert(_fieldMetadata.autocompleteColumnId != null, 'FieldMetadata must be a AutocompleteField');
  }

  @override
  FutureOr<List<KeyLabel<T>>> findByQuery(String? query) async {
    final response = await http.post(
      "/autocomplete/find",
      body: {
        "autocompleteFieldName": autocompleteMetadataName,
        "query": query,
        "defaultWhere": defaultWhereBuilder?.call()?.toJson(),
      },
    );

    return response.bodyListLowerCaseMap.map((e) => RemoteKeyLabel<T>.fromMap(e)).toList();
  }

  @override
  KeyLabel<T>? fromValue(dynamic value) {
    if (value == null) return null;
    if (value is! Map<String, dynamic>) throw 'Value must be a Map<String, dynamic> to AutocompleteField $autocompleteMetadataName';
    final data = LowercaseMap.fromMap(value);

    final id = data[_fieldMetadata.autocompleteColumnId] as T?;
    if (id == null) return null;

    final labels = _fieldMetadata.autocompleteColumnsView!
        .split(',')
        .map(
          (e) => data[e.trim().toLowerCase()],
        )
        .where(
          (v) => v != null,
        )
        .join('-');

    return RemoteKeyLabel<T>(data[_fieldMetadata.autocompleteColumnId] as T, labels, data);
  }
}
