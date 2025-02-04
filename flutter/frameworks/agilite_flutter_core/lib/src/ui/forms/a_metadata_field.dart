import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const String MOD_FONE = 'fone';
const String MOD_CEP = 'cep';
const String MOD_UF = 'uf';
const String MOD_NI = 'ni';
const String MOD_NITIPO = 'niTipo';

//Apenas modificadores de colunas
const String MOD_STATUS_DATE = 'statusDate';

class AMetadataField extends StatelessWidget {
  final String fieldName;
  final void Function(dynamic controller)? controllerCreated;

  //Customizações
  final FieldMetadataType? metadataType;
  final String? labelText;
  final int? maxLength;
  final int? maxLines;
  final String? hintText;
  final String? helperText;
  final bool? enabled;
  final String? mod;

  //Combo
  final List<Option<dynamic>>? autoCompleteOptions;

  //Autocomplete by FK
  final ClientWhere? Function()? defaultWhereBuilder;

  const AMetadataField(
    this.fieldName, {
    this.controllerCreated,
    this.labelText,
    this.hintText,
    this.helperText,
    this.metadataType,
    this.autoCompleteOptions,
    this.maxLength,
    this.maxLines,
    this.defaultWhereBuilder,
    this.enabled,
    this.mod,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final metadata = metadataRepository.field(fieldName);

    if (mod != null || metadata.mod != null) {
      return _buildByModification(metadata);
    }

    if (autoCompleteOptions != null || metadata.options != null) {
      return _buildAComboField(metadata);
    }

    if (metadata.autocompleteColumnId != null) {
      return _buildAutocompleteField(metadata);
    }

    switch (metadataType ?? metadata.type) {
      case FieldMetadataType.string:
      case FieldMetadataType.int:
      case FieldMetadataType.double:
      case FieldMetadataType.date:
        return _buildATextField(metadata);
      case FieldMetadataType.bool:
        return _buildCheckboxField(metadata);
      default:
        throw 'AMetadataField for type ${metadata.type} not implemented';
    }
  }

  Widget _buildAComboField(FieldMetadata field, [List<Option<dynamic>>? localAutoCompleteOptions = null]) {
    final options = localAutoCompleteOptions ?? autoCompleteOptions ?? field.options!.map((e) => LocalOption(e.value, e.label)).toList();
    if (options.length < 15) {
      if (!field.req && options[0].jsonKey != null) {
        options.insert(0, LocalOption<Object>(null, "Selecione..."));
      }

      return AComboField(
        field.name,
        options: options,
        labelText: labelText ?? field.label,
        onControllerCreated: onControllerCreated,
        validators: ValidationMapper.parseValidationQuery(field.validationQuery),
        hintText: hintText,
        helperText: helperText,
        enabled: enabled,
      );
    } else {
      return AAutocompleteField.options(
        field.name,
        options: options,
        labelText: labelText ?? field.label,
        req: field.req,
        onControllerCreated: onControllerCreated,
        validators: ValidationMapper.parseValidationQuery(field.validationQuery),
        hintText: hintText,
        helperText: helperText,
        enabled: enabled,
      );
    }
  }

  Widget _buildAutocompleteField(FieldMetadata field) {
    return AAutocompleteField.api(
      field.name,
      labelText: labelText ?? field.label,
      req: field.req,
      onControllerCreated: onControllerCreated,
      validators: ValidationMapper.parseValidationQuery(field.validationQuery),
      hintText: hintText,
      helperText: helperText,
      defaultWhereBuilder: defaultWhereBuilder,
      enabled: enabled,
    );
  }

  Widget _buildByModification(FieldMetadata metadata) {
    final mod = this.mod ?? metadata.mod;
    return switch (mod) {
      MOD_FONE => ATextFieldFone(
          metadata.name,
          labelText: labelText ?? metadata.label,
          req: metadata.req,
        ),
      MOD_CEP => ATextFieldCep(
          metadata.name,
          labelText: labelText ?? metadata.label,
          req: metadata.req,
        ),
      MOD_NI => ATextFieldNi(
          metadata.name,
          labelText: labelText ?? metadata.label,
          req: metadata.req,
        ),
      MOD_UF => _buildAComboField(metadata, ufComboOptions),
      MOD_NITIPO => AComboNiTipo(
          metadata.name,
          labelText: labelText ?? metadata.label,
          req: metadata.req,
        ),
      _ => throw 'Mod $mod not implemented',
    };
  }

  Widget _buildATextField(FieldMetadata field) {
    final FieldType type = switch (field.type) {
      FieldMetadataType.string => FieldType.string,
      FieldMetadataType.int => FieldType.int,
      FieldMetadataType.double => FieldType.double,
      FieldMetadataType.date => FieldType.date,
      _ => throw 'Field type ${field.type} not implemented',
    };

    return ATextField.all(
      type,
      field.name,
      labelText: labelText ?? field.label,
      req: field.req,
      maxLength: _getTextFieldMaxLength(field),
      maxDecimalDigits: _getMaxDecimalDigits(field),
      minDecimalDigits: _getMinDecimalDigits(field),
      maxIntegerDigits: _getMaxIntegerDigits(field),
      maxLines: maxLines ?? _getMaxLines(field),
      onControllerCreated: onControllerCreated,
      validators: ValidationMapper.parseValidationQuery(field.validationQuery),
      hintText: hintText,
      helperText: helperText,
      enabled: enabled,
    );
  }

  Widget _buildCheckboxField(FieldMetadata field) {
    return ABoolField(
      field.name,
      labelText: labelText ?? field.label,
      onControllerCreated: onControllerCreated,
      enabled: enabled,
    );
  }

  void onControllerCreated(FieldController controller) {
    controllerCreated?.call(controller);
  }

  int? _getMaxLines(FieldMetadata field) {
    if (field.type == FieldMetadataType.string && field.size != null && field.size! == 0) {
      return 5;
    }
    return null;
  }

  int? _getMaxDecimalDigits(FieldMetadata field) {
    if (field.type == FieldMetadataType.double) {
      String decimalString = field.size.toString().split('.')[1];
      return int.parse(decimalString);
    }
    return null;
  }

  int? _getMinDecimalDigits(FieldMetadata field) {
    if (field.type == FieldMetadataType.double) {
      return 2;
    }
    return null;
  }

  int? _getMaxIntegerDigits(FieldMetadata field) {
    if (field.type == FieldMetadataType.double) {
      return field.size!.toInt();
    }
    return null;
  }

  int? _getTextFieldMaxLength(FieldMetadata field) {
    if (field.size == null || field.size == 0) {
      return null;
    }
    if (field.type == FieldMetadataType.string) {
      return field.size!.toInt();
    }

    return null;
  }
}
