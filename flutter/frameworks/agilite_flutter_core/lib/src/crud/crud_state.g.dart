// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'crud_state.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CrudState _$CrudStateFromJson(Map<String, dynamic> json) => CrudState(
      currentPage: (json['currentPage'] as num).toInt(),
      pageSize: (json['pageSize'] as num).toInt(),
      data: (json['data'] as List<dynamic>)
          .map((e) => e as Map<String, dynamic>)
          .toList(),
      groups: (json['groups'] as List<dynamic>?)
          ?.map((e) => CrudStatusGroup.fromJson(e as Map<String, dynamic>))
          .toList(),
      selectedGroupIndex: (json['selectedGroupIndex'] as num?)?.toInt(),
    );

CrudStatusGroup _$CrudStatusGroupFromJson(Map<String, dynamic> json) =>
    CrudStatusGroup(
      (json['qtd'] as num).toInt(),
      json['title'] as String,
      subtitle: json['subtitle'] as String?,
    );
