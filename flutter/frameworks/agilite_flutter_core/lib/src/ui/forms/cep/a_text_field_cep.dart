import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'cep.dart';

class ATextFieldCep extends StatefulWidget {
  final FormController? formController;
  final String name;
  final String? labelText;
  final void Function(Cep cep)? onCepFound;

  const ATextFieldCep(
    this.name, {
    this.formController,
    this.labelText,
    this.onCepFound,
    super.key,
  });

  @override
  State<ATextFieldCep> createState() => _ATextFieldCepState();
}

class _ATextFieldCepState extends State<ATextFieldCep> {
  bool _searching = false;
  late FormController formController;

  @override
  void initState() {
    final formState = context.findAncestorStateOfType<AFormState>();
    final localFormController = widget.formController ?? formState?.controller;
    if (localFormController == null) {
      throw 'Não foi possível localizar o FormController para criar o campo CEP';
    }

    formController = localFormController;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return ATextField.string(
      widget.name,
      inputFormatters: [FilteringTextInputFormatter.digitsOnly, CepTextInputFormatter()],
      onControllerCreated: (controller) => _configureController(controller as StringController),
      decoration: InputDecoration(
        labelText: widget.labelText ?? 'CEP',
        suffixIcon: _searching
            ? Transform.scale(
                scale: 0.7,
                child: const CircularProgressIndicator(strokeWidth: 2),
              )
            : IconButton(
                icon: const Icon(Icons.search),
                onPressed: _findCep,
              ),
      ),
    );
  }

  void _configureController(StringController controller) {
    controller.focusNode.addListener(() {
      if (!controller.focusNode.hasFocus) {
        controller.value = controller.value;
      }
    });

    controller.parseValue = (text) => text.onlyNumbers();
    controller.formatValue = (value) {
      if (value.isNullOrBlank) return '';
      if (value!.length == 8) {
        return '${value.substring(0, 5)}-${value.substring(5)}';
      } else {
        return value;
      }
    };
  }

  void _findCep() async {
    final cep = formController.getController(widget.name)?.value as String?;
    if (cep.isNullOrBlank) {
      showWarningSnack('Informe um CEP válido para buscar o endereço');
      return;
    }

    setState(() {
      _searching = true;
    });
    try {
      final endereco = await coreCepService.find(cep!);

      if (widget.onCepFound == null) {
        _showCep(endereco);
      } else {
        widget.onCepFound!(endereco);
      }
    } on ValidationException catch (e) {
      showWarningSnack(e.message);
    } catch (e) {
      //TODO bugsnag
      showErrorSnack("Erro ao buscar CEP");
    } finally {
      setState(() {
        _searching = false;
      });
    }
  }

  void _showCep(Cep cep) {
    final tableName = FieldMetadata.extractTableName(widget.name);

    formController.showValues({
      '${tableName}endereco': cep.logradouro,
      '${tableName}bairro': cep.bairro,
      '${tableName}complemento': cep.complemento,
      '${tableName}municipio': cep.localidade,
      '${tableName}uf': cep.uf,
    });

    formController.getController('${tableName}numero')?.focusNode.requestFocus();
  }
}

class CepTextInputFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(TextEditingValue oldValue, TextEditingValue newValue) {
    final newText = newValue.text;
    if (newText.length > 8) {
      return oldValue;
    }
    return newValue;
  }
}
