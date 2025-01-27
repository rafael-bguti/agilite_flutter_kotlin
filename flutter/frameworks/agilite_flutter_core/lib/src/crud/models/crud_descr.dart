import 'package:json_annotation/json_annotation.dart';

part 'crud_descr.g.dart';

@JsonSerializable()
class CrudDescr {
  final String singular;
  final String? _plural;

  const CrudDescr(
    this.singular, {
    String? plural,
  }) : _plural = plural;

  String get plural {
    return _plural ?? '${singular}s';
  }

  factory CrudDescr.fromJson(Map<String, dynamic> json) => _$CrudDescrFromJson(json);
}
