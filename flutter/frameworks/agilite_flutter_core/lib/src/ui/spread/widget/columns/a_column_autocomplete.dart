import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

enum _Type {
  options,
  combo,
  api,
}

class AColumnAutocomplete<T> extends ASpreadColumn<T> {
  final List<KeyLabel<T>>? options;
  final ClientWhere? Function()? defaultWhereBuilder;
  final Widget Function(KeyLabel<T>)? listItemBuilder;
  final _Type _type;

  final void Function(KeyLabel<T>? value, int row)? onSelectedValue;
  late final AutocompleteController<T> _fieldController;

  final String? autocompleteMetadataName;

  AColumnAutocomplete.options(
    super.name,
    super.label, {
    super.isEditable,
    required this.options,
    this.listItemBuilder,
    this.onSelectedValue,
  })  : _type = _Type.options,
        defaultWhereBuilder = null,
        autocompleteMetadataName = null,
        super(alignment: Alignment.centerLeft) {
    _fieldController = _buildComboController();
  }

  AColumnAutocomplete.combo(
    super.name,
    super.label, {
    super.isEditable,
    required this.options,
    this.listItemBuilder,
    this.onSelectedValue,
  })  : _type = _Type.combo,
        defaultWhereBuilder = null,
        autocompleteMetadataName = null,
        super(alignment: Alignment.centerLeft) {
    _fieldController = _buildComboController(combo: true);
  }

  AColumnAutocomplete.api(
    super.name,
    super.label, {
    this.autocompleteMetadataName,
    super.isEditable,
    this.listItemBuilder,
    this.defaultWhereBuilder,
    super.req,
    super.validators,
    this.onSelectedValue,
  })  : _type = _Type.api,
        options = null,
        super(alignment: Alignment.centerLeft) {
    _fieldController = _buildApiController();
  }

  String? lastPressedChar;
  @override
  void onEdit(int row, String? pressedChar) {
    lastPressedChar = pressedChar;
  }

  @override
  Widget buildEditCell(BuildContext context, int row) {
    final actualValue = _fieldController.extractValueFromRemoteJson(spreadController.value[row].toMap());
    _fieldController.value = actualValue;
    _fieldController.selectedIndex.value = -1;
    runOnNextBuild(() {
      if (lastPressedChar != null) {
        _fieldController.searchEditingController.text = lastPressedChar ?? '';
        _fieldController.showMenu(false);
      } else {
        _fieldController.showMenu();
      }
    });

    return _getTextField();
  }

  @override
  Widget buildRenderCell(BuildContext context, int row, bool isFocused) {
    final value = _fieldController.extractValueFromRemoteJson(spreadController.value[row].toMap());

    return Row(
      children: [
        Expanded(
          child: Text(value?.label ?? ''),
        ),
        const Padding(
          padding: EdgeInsets.only(right: 3.0),
          child: Icon(Icons.arrow_drop_down, size: 14),
        )
      ],
    );
    // }
  }

  @override
  void doRequestFocusOnEdit() {
    _fieldController.searchFocusNode.requestFocus();
  }

  @override
  void stopEditing(int rowIndex) {
    if (_fieldController.menuController.isOpen) _fieldController.menuController.close();
    final keyLabel = _fieldController.value;
    _addValueOnSpread(keyLabel, rowIndex);
  }

  void _addValueOnSpread(KeyLabel<T>? keyLabel, int rowIndex) {
    this.onSelectedValue?.call(keyLabel, rowIndex);
    if (keyLabel != null) {
      spreadController.value[rowIndex][name] = keyLabel.jsonKey;
    } else {
      spreadController.value[rowIndex].remove(name);
    }

    spreadController.onCellStopEdit?.call(spreadController, rowIndex, name);
  }

  @override
  //Retorna null pois essa coluna trata o StopEdit diretamente
  T? getValueOnStopEdit() => null;

  @override
  void dispose() {
    _fieldController.dispose();
    super.dispose();
  }

  @override
  Future<void> normalizeSpreadValue(List<Map<String, dynamic>> value) async {}

  AAutocompleteField<T>? _textField;
  AAutocompleteField<T> _getTextField() {
    return _textField ??= AAutocompleteField<T>(
      _fieldController,
      listItemBuilder: listItemBuilder,
      decoration: const InputDecoration(
        border: InputBorder.none,
        contentPadding: EdgeInsets.all(0),
      ),
    );
  }

  @override
  Widget buildTextInputFromColumn(
    BuildContext context, {
    String? name,
    String? labelText,
    InputDecoration? inputDecoration,
    bool? autoFocus,
  }) {
    switch (_type) {
      case _Type.combo:
        return AAutocompleteField.combo(
          name ?? this.name,
          options: options!,
          labelText: labelText ?? label,
          autoFocus: autoFocus,
        );
      case _Type.api:
        return AAutocompleteField.api(
          name ?? this.name,
          autoFocus: autoFocus,
          labelText: labelText ?? label,
          autocompleteMetadataName: autocompleteMetadataName,
        );
      case _Type.options:
        return AAutocompleteField.options(
          name ?? this.name,
          options: options!,
          labelText: labelText ?? label,
          decoration: inputDecoration,
          autoFocus: autoFocus,
        );
    }
  }

  AutocompleteController<T> _buildComboController({bool combo = false}) {
    return AutocompleteController<T>(
      name,
      repository: AllInMemoryAutocompleteRepository<T>(options!),
      createdBySpreadColumn: true,
      showAllOptions: combo,
    );
  }

  AutocompleteController<T> _buildApiController() {
    final repository = RemoteAPIAutocompleteRepository<T>(
      http: coreHttpProvider,
      autocompleteMetadataName: autocompleteMetadataName ?? name,
      defaultWhereBuilder: defaultWhereBuilder,
    );

    return AutocompleteController<T>(
      name,
      repository: repository,
      createdBySpreadColumn: true,
    );
  }
}
