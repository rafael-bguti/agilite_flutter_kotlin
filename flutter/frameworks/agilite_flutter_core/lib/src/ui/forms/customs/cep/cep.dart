import 'package:json_annotation/json_annotation.dart';

part 'cep.g.dart';

@JsonSerializable()
class Cep {
  final String cep;
  final String logradouro;
  final String complemento;
  final String bairro;
  final String localidade;
  final String uf;

  Cep({
    required this.cep,
    required this.logradouro,
    required this.complemento,
    required this.bairro,
    required this.localidade,
    required this.uf,
  });

  factory Cep.fromJson(Map<String, dynamic> json) => _$CepFromJson(json);
  Map<String, dynamic> toJson() => _$CepToJson(this);
}
