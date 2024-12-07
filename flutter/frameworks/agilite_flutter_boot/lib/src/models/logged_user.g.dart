// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'logged_user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

LoggedUser _$LoggedUserFromJson(Map<String, dynamic> json) => LoggedUser(
      nome: json['nome'] as String,
      email: json['email'] as String,
      token: json['token'] as String,
      refreshToken: json['refreshToken'] as String,
      empresa: json['empresa'] as String,
      menu: (json['menu'] as List<dynamic>)
          .map((e) => MenuItem.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$LoggedUserToJson(LoggedUser instance) =>
    <String, dynamic>{
      'nome': instance.nome,
      'email': instance.email,
      'empresa': instance.empresa,
      'token': instance.token,
      'refreshToken': instance.refreshToken,
      'menu': instance.menu,
    };
