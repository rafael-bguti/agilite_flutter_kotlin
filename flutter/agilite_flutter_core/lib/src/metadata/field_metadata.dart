import 'package:agilite_flutter_core/core.dart';
import 'package:json_annotation/json_annotation.dart';

part 'field_metadata.g.dart';

enum FieldMetadataType { string, int, double, bool, date, time, timestamp, map, autocomplete }

@JsonSerializable()
class FieldMetadata {
  final String name;
  final String label;
  final FieldMetadataType type;
  final bool req;

  final String? hintText;
  final String? helperText;

  final double? size;
  final String? validationQuery;

  //---- OPTIONS ----
  final List<FieldMetadataOption>? options;

  //---- Autocomplete Config ----
  final String? autocompleteColumnId;
  final String? autocompleteColumnsView;

  const FieldMetadata({
    required this.name,
    required this.label,
    required this.type,
    this.req = false,
    this.size,
    this.options,
    this.validationQuery,
    this.hintText,
    this.helperText,
    this.autocompleteColumnId,
    this.autocompleteColumnsView,
  });

  factory FieldMetadata.fromJson(Map<String, dynamic> json) => _$FieldMetadataFromJson(json);
  Map<String, dynamic> toJson() => _$FieldMetadataToJson(this);

  static String extractTableName(String fieldName) {
    var entityName = fieldName.substr(0, 5);
    var i = 5;
    while (fieldName[i].isDigit) {
      entityName = fieldName[i];
      i++;
    }
    return entityName;
  }
}
