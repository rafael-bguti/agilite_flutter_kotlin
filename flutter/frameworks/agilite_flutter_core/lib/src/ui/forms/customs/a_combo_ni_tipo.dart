import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AComboNiTipo extends StatefulWidget {
  final String name;
  final String? labelText;
  final bool? req;

  const AComboNiTipo(
    this.name, {
    this.labelText,
    this.req,
    super.key,
  });

  @override
  State<AComboNiTipo> createState() => _AComboNiTipoState();
}

class _AComboNiTipoState extends State<AComboNiTipo> {
  late FormController formController;

  @override
  void initState() {
    final formState = context.findAncestorStateOfType<AFormState>();
    if (formState?.controller == null) {
      throw 'Não foi possível localizar o FormController para criar o campo NiTipo';
    }

    formController = formState!.controller;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return AComboField(
      widget.name,
      options: _getOptions(),
      labelText: widget.labelText,
      onControllerCreated: (controller) {
        controller.addValueChangeListener(_onValueChanged);
      },
    );
  }

  List<Option<int?>> _getOptions() {
    final result = widget.req == true ? <Option<int>>[] : [LocalOption<int>(null, 'Selecione')];
    result.addAll(_options);
    return result;
  }

  void _onValueChanged() {
    final value = formController.getInt(widget.name);
    final String label;
    if (value == null) {
      label = 'CPF/CNPJ';
    } else {
      label = switch (value) {
        0 => 'CPF',
        1 => 'CNPJ',
        _ => 'Documento',
      };
    }
    final tableName = FieldMetadata.extractTableName(widget.name);
    formController.getController("${tableName}ni")?.labelText = label;
  }

  final _options = [
    LocalOption<int>(0, 'Pessoa Física'),
    LocalOption<int>(1, 'Pessoa Jurídica'),
    LocalOption<int>(2, 'Estrangeiro'),
  ];
}
