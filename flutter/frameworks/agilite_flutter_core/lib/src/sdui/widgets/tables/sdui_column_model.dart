import 'package:agilite_flutter_core/core.dart';
import 'package:json_annotation/json_annotation.dart';

part 'sdui_column_model.g.dart';

@JsonSerializable(createToJson: false)
class SduiColumnModel {
  final String name;
  final String? label;
  final FieldMetadataType type;
  final List<LocalOption>? options;
  final String? mod;

  final AWidth? width;

  SduiColumnModel({
    required this.name,
    required this.type,
    this.label,
    this.options,
    this.width,
    this.mod,
  });

  factory SduiColumnModel.fromJson(Map<String, dynamic> json) => _$SduiColumnModelFromJson(json);
}
