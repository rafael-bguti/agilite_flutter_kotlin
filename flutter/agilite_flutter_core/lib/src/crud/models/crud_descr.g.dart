// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'crud_descr.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CrudDescr _$CrudDescrFromJson(Map<String, dynamic> json) => CrudDescr(
      json['singular'] as String,
      plural: json['plural'] as String?,
    );

Map<String, dynamic> _$CrudDescrToJson(CrudDescr instance) => <String, dynamic>{
      'singular': instance.singular,
      'plural': instance.plural,
    };
