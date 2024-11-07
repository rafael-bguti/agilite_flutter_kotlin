import 'package:agilite_flutter_core/core.dart';
import 'package:json_annotation/json_annotation.dart';

part 'logged_user.g.dart';

@JsonSerializable()
class LoggedUser {
  final String nome;
  final String email;
  final String empresa;
  final String token;
  final String refreshToken;
  final List<MenuItem> menu;

  LoggedUser({
    required this.nome,
    required this.email,
    required this.token,
    required this.refreshToken,
    required this.empresa,
    required this.menu,
  });

  factory LoggedUser.fromJson(Map<String, dynamic> json) => _$LoggedUserFromJson(json);
  Map<String, dynamic> toJson() => _$LoggedUserToJson(this);
}
