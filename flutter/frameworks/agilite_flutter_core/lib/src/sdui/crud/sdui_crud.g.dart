// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_crud.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiCrudModel _$SduiCrudModelFromJson(Map<String, dynamic> json) =>
    SduiCrudModel(
      id: json['id'] as String?,
      taskName: json['taskName'] as String,
      descr: CrudDescr.fromJson(json['descr'] as Map<String, dynamic>),
      columns: (json['columns'] as List<dynamic>)
          .map((e) => SduiSpreadColumnModel.fromJson(e as Map<String, dynamic>))
          .toList(),
      customFilters: (json['customFilters'] as List<dynamic>?)
          ?.map((e) => e as Map<String, dynamic>)
          .toList(),
    );
