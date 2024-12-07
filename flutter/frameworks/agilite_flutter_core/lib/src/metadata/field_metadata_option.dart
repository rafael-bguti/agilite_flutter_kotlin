import 'package:json_annotation/json_annotation.dart';

part 'field_metadata_option.g.dart';

@JsonSerializable()
class FieldMetadataOption {
  final Object value;
  final String label;

  FieldMetadataOption(this.value, this.label);

  factory FieldMetadataOption.fromJson(Map<String, dynamic> json) => _$FieldMetadataOptionFromJson(json);
  Map<String, dynamic> toJson() => _$FieldMetadataOptionToJson(this);
}
