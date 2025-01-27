// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sdui_combo_field.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SduiComboFieldModel _$SduiComboFieldModelFromJson(Map<String, dynamic> json) =>
    SduiComboFieldModel(
      name: json['name'] as String,
      options: (json['options'] as List<dynamic>)
          .map((e) => LocalOption<dynamic>.fromJson(e as Map<String, dynamic>))
          .toList(),
      labelText: json['labelText'] as String?,
      hintText: json['hintText'] as String?,
      helperText: json['helperText'] as String?,
      enabled: json['enabled'] as bool?,
      id: json['id'] as String?,
    );
