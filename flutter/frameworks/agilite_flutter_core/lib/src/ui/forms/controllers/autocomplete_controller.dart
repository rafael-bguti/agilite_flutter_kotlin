import 'dart:async';
import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AutocompleteController<T> extends FieldController<KeyLabel<T>?> {
  List<KeyLabel<T>>? allOptions;
  final List<KeyLabel<T>> filteredOptions = [];
  final AutocompleteRepository<T> repository;

  String? _helperText;
  String? _hintText;

  final TextEditingController comboEditingController;
  final menuController = MenuController();
  final FocusNode suffixIconFocusNode;

  final KeyValueConverter<T> keyValueConverter;

  final bool showAllOptions;

  final void Function(KeyLabel<T>? value)? onSelectedValue;

  final FocusNode searchFocusNode = FocusNode();
  final TextEditingController searchEditingController = TextEditingController();

  final ValueNotifier<bool> loading;
  final ValueNotifier<int> selectedIndex;
  final ScrollController itemsScrollController;

  AutocompleteController(
    super.name, {
    required this.repository,
    super.defaultValue,
    super.initialValue,
    super.enabled,
    super.validators,
    super.labelText,
    super.req,
    super.createdBySpread,
    super.autoFocus,
    this.showAllOptions = false,
    this.keyValueConverter = const KeyValueConverter(),
    String? helperText,
    String? hintText,
    this.onSelectedValue,
  })  : _helperText = helperText,
        _hintText = hintText,
        selectedIndex = ValueNotifier(-1),
        loading = ValueNotifier(false),
        itemsScrollController = ScrollController(),
        comboEditingController = TextEditingController(),
        suffixIconFocusNode = FocusNode(skipTraversal: true) {
    focusNode.onKey = _onComboKeyPressed;
    searchFocusNode.onKey = _onSearchKeyPressed;

    focusNode.addListener(() async {
      if (focusNode.hasFocus) {
        selectAllText();
      }
    });
  }

  //---- Override -----
  @override
  void dispose() {
    selectedIndex.dispose();
    loading.dispose();
    itemsScrollController.dispose();
    comboEditingController.dispose();
    suffixIconFocusNode.dispose();

    searchFocusNode.dispose();
    searchEditingController.dispose();

    super.dispose();
  }

  KeyLabel<T>? _value;
  @override
  KeyLabel<T>? get value => _value;
  @override
  set value(KeyLabel<T>? localValue) {
    if (localValue != _value) {
      _updateValue(localValue);
    }
  }

  void _updateValue(KeyLabel<T>? localValue, [bool fromUserAction = true]) {
    comboEditingController.text = keyValueConverter.valueToString(localValue);
    _value = localValue ?? defaultValue;
    onValueChanged.notifyListeners();
    notifyListeners();

    status = FieldStatus.dirty;
    if (fromUserAction) onSelectedValue?.call(value);
    validate();
  }

  @override
  get jsonValue => value?.jsonKey;

  @override
  void fillFromJson(Map<String, dynamic>? data) {
    final keyVal = extractValueFromRemoteJson(data);
    _updateValue(keyVal, false);
  }

  KeyLabel<T>? extractValueFromRemoteJson(Map<String, dynamic>? data) {
    if (data == null) return null;

    final value = data[name];
    return repository.fromValue(value);
  }

  //--- Controle de visibilidade do menu ----
  void toggleMenu() {
    if (menuController.isOpen) {
      focusNode.requestFocus();
      closeMenu();
    } else {
      searchFocusNode.requestFocus();
      showMenu();
    }
  }

  void showMenu([bool clearSearch = true]) {
    if (menuController.isOpen) return;
    searchFocusNode.requestFocus();
    menuController.open();

    if (clearSearch) searchEditingController.text = '';
    onSearchTextChange(searchEditingController.text.trim());
  }

  void closeMenu() {
    if (!menuController.isOpen) return;
    focusNode.requestFocus();
    menuController.close();
  }

  void selectAllText([bool selectIfCreatedBySpread = false]) {
    if (createdBySpread && !selectIfCreatedBySpread) return;
    if (comboEditingController.text.isNotEmpty) {
      comboEditingController.selection = TextSelection(baseOffset: 0, extentOffset: comboEditingController.text.length);
    }
  }

  //---- Keyboard Actions ----
  KeyEventResult _onComboKeyPressed(FocusNode focusNode, RawKeyEvent key) {
    if (key is RawKeyDownEvent) {
      final handlers = {
        LogicalKeyboardKey.arrowDown: () => _onArrowKeyPressed(1),
        LogicalKeyboardKey.enter: _onComboEnterKeyPressed,
        LogicalKeyboardKey.escape: _onEscKeyPressed,
        LogicalKeyboardKey.delete: () {
          if (value != null) {
            value = null;
          }
          return KeyEventResult.handled;
        },
      };
      final handler = handlers[key.logicalKey]?.call();
      if (handler != null) {
        return handler;
      } else if (key.character != null) {
        searchEditingController.text = key.character!;
        showMenu(false);
        return KeyEventResult.handled;
      }
    }
    return KeyEventResult.ignored;
  }

  KeyEventResult _onSearchKeyPressed(FocusNode focusNode, RawKeyEvent key) {
    if (key is RawKeyDownEvent) {
      final handlers = {
        LogicalKeyboardKey.arrowDown: () => _onArrowKeyPressed(1),
        LogicalKeyboardKey.arrowUp: () => _onArrowKeyPressed(-1),
        LogicalKeyboardKey.enter: _onSearchEnterKeyPressed,
        LogicalKeyboardKey.escape: _onEscKeyPressed,
      };

      return handlers[key.logicalKey]?.call() ?? KeyEventResult.ignored;
    }
    return KeyEventResult.ignored;
  }

  KeyEventResult _onArrowKeyPressed(int delta) {
    if (delta == 1 && !menuController.isOpen) {
      showMenu();
      return KeyEventResult.handled;
    }

    final options = filteredOptions;
    final newIndex = selectedIndex.value + delta;
    if (newIndex >= 0 && newIndex < options.length) {
      selectedIndex.value = newIndex;
      _moveScrollToItem();
    }

    return KeyEventResult.handled;
  }

  void _moveScrollToItem() {
    if (!itemsScrollController.hasClients) return;

    const itemHeight = 50.0;
    final currentScroll = itemsScrollController.position.pixels;
    final viewport = itemsScrollController.position.viewportDimension;

    final startItem = selectedIndex.value * itemHeight;
    final endItem = startItem + itemHeight;

    final endScroll = currentScroll + viewport;

    bool isCompleteVisible = startItem >= currentScroll && endItem <= endScroll;
    if (isCompleteVisible) return;

    var newPosition = 0.0;
    if (endItem > endScroll) {
      newPosition = endItem - viewport;
    } else if (startItem < currentScroll) {
      newPosition = startItem;
    }

    final maxScroll = itemsScrollController.position.maxScrollExtent;
    itemsScrollController.jumpTo(max(min(newPosition, maxScroll), 0));
  }

  KeyEventResult _onSearchEnterKeyPressed() {
    if (menuController.isOpen) {
      value = _selectedOption();
      closeMenu();
    }

    return createdBySpread ? KeyEventResult.ignored : KeyEventResult.handled;
  }

  KeyEventResult _onComboEnterKeyPressed() {
    if (menuController.isOpen) {
      closeMenu();
    }

    return createdBySpread ? KeyEventResult.ignored : KeyEventResult.handled;
  }

  KeyEventResult _onEscKeyPressed() {
    if (menuController.isOpen) {
      closeMenu();
      return createdBySpread ? KeyEventResult.ignored : KeyEventResult.handled;
    } else {
      return KeyEventResult.ignored;
    }
  }

  //---- Setters e Getters ----
  String? get helperText => _helperText;
  set helperText(String? helperText) {
    if (helperText != _helperText) {
      _helperText = helperText;
      notifyListeners();
    }
  }

  String? get hintText => _hintText;
  set hintText(String? hintText) {
    if (hintText != _hintText) {
      _hintText = hintText;
      notifyListeners();
    }
  }

  void onListItemTap(KeyLabel<T> option) {
    value = option;
    closeMenu();
  }

  void onSearchTextChange(String value) {
    try {
      selectedIndex.value = value.isEmpty || showAllOptions ? -1 : 0;
      loadingOptions(value);
    } catch (e) {
      print(e); //TODO Criar uma classe de erro que indique para o tratador global que é pra exibir uma msg global
      rethrow;
    }
  }

  Future<void> loadingOptions(String? query) async {
    if (showAllOptions) {
      await _navigateWhenComboBox(query?.trim());
    } else {
      await _loadWhenAutocomplete(query?.trim());
    }
  }

  Future<void> _navigateWhenComboBox(String? query) async {
    allOptions ??= await repository.findByQuery(null);
    selectedIndex.value = query.isNullOrBlank ? -1 : allOptions!.indexWhere((e) => e.toString().trim().toLowerCase().startsWith(query!.toLowerCase()));
    filteredOptions.clear();
    filteredOptions.addAll(allOptions!);
    _moveScrollToItem();
  }

  Future<void> _loadWhenAutocomplete(String? query) async {
    final optionsData = repository.findByQuery(query);
    filteredOptions.clear();
    loading.value = true;
    try {
      filteredOptions.addAll(await optionsData);
    } finally {
      loading.value = false;
    }
  }

  KeyLabel<T>? _selectedOption() {
    final selIndex = selectedIndex.value;
    if (selIndex >= 0 && selIndex < filteredOptions.length) {
      return filteredOptions[selIndex];
    }
    return null;
  }
}

class KeyValueConverter<T> {
  const KeyValueConverter();

  String valueToString(KeyLabel<dynamic>? value) {
    if (value == null) return '';
    return value.toString();
  }
}
