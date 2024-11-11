import 'package:agilite_flutter_core/core.dart';

abstract class MetadataRepository {
  Future<void> loadFieldsByNames(List<String> entity);

  FieldMetadata field(String name);

  FieldMetadataOption option(String field, Object value);
}

class MetadataRepositoryImpl implements MetadataRepository {
  final HttpProvider provider;
  static final Set<String> loadedEntities = {};
  //TODO - Precisa adicionar uma forma de limpar o cache talvez com um time ou um version no servidor, pois caso contrário quando atualizar a versão no servidor os campos não serão atualizados
  static final LowercaseMap _fields = LowercaseMap();

  MetadataRepositoryImpl(this.provider);

  @override
  Future<void> loadFieldsByNames(List<String> entities) async {
    entities = entities.where((entity) => !loadedEntities.contains(entity)).toList();
    if (entities.isEmpty) {
      return;
    }

    final response = await provider.post('/public/metadata/load', body: entities);
    final loadedFields = response.bodyListLowerCaseMap.map((element) {
      return FieldMetadata.fromJson(element);
    }).toList();

    for (final field in loadedFields) {
      _fields[field.name] = field;
    }

    loadedEntities.addAll(entities);
  }

  @override
  FieldMetadata field(String name) {
    final field = _fields[name.toLowerCase()];
    if (field == null) {
      throw 'Field $name not found on loaded entities';
    }

    return field as FieldMetadata;
  }

  @override
  FieldMetadataOption option(String field, Object value) {
    final fieldMetadata = _fields[field];
    if (fieldMetadata == null) {
      throw 'Field $field not found on loaded entities';
    }

    final option = fieldMetadata.options!.firstWhere((element) => element.value == value, orElse: () {
      throw 'Option $value not found on field $field';
    });

    return option as FieldMetadataOption;
  }
}
