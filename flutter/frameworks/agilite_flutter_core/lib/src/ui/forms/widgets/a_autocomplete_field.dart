import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _kMinWidth = 120.0;

final _selectedBackgroundColor = onBackgroundColor.withOpacity(0.4);

class AAutocompleteField<T> extends StatefulWidget {
  final String? name;
  final AutocompleteController<T>? fieldController;

  final InputDecoration? decoration;
  final Widget Function(Option<T>)? listItemBuilder;

  final String? labelText;
  final String? hintText;
  final String? helperText;
  final bool? enabled;
  final List<FieldValidator>? validators;

  final bool? req;
  final bool? autoFocus;
  final bool? showAllOptions;

  //Default Where only to Api Repository
  final ClientWhere? Function()? defaultWhereBuilder;

  final void Function(AutocompleteController<T> controller)? onControllerCreated;
  final void Function(Option<T>? value)? onSelectedValue;

  final String? autocompleteMetadataName;
  final bool isApi;

  const AAutocompleteField(
    this.fieldController, {
    super.key,
    this.listItemBuilder,
    this.decoration,
  })  : labelText = null,
        hintText = null,
        helperText = null,
        enabled = null,
        validators = null,
        name = null,
        options = null,
        showAllOptions = null,
        req = null,
        autoFocus = null,
        onControllerCreated = null,
        defaultWhereBuilder = null,
        autocompleteMetadataName = null,
        onSelectedValue = null,
        isApi = false;

  final List<Option<T>>? options;
  const AAutocompleteField.options(
    this.name, {
    required List<Option<T>> this.options,
    super.key,
    this.listItemBuilder,
    this.decoration,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.validators,
    this.req,
    this.showAllOptions,
    this.autoFocus,
    this.onControllerCreated,
    this.onSelectedValue,
  })  : fieldController = null,
        autocompleteMetadataName = null,
        defaultWhereBuilder = null,
        isApi = false;

  const AAutocompleteField.combo(
    this.name, {
    required List<Option<T>> this.options,
    super.key,
    this.listItemBuilder,
    this.decoration,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.validators,
    this.req,
    this.autoFocus,
    this.onControllerCreated,
    this.onSelectedValue,
  })  : fieldController = null,
        showAllOptions = true,
        autocompleteMetadataName = null,
        defaultWhereBuilder = null,
        isApi = false;

  const AAutocompleteField.api(
    this.name, {
    this.autocompleteMetadataName,
    super.key,
    this.listItemBuilder,
    this.decoration,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled,
    this.validators,
    this.req,
    this.autoFocus,
    this.onControllerCreated,
    this.defaultWhereBuilder,
    this.onSelectedValue,
  })  : fieldController = null,
        showAllOptions = false,
        options = null,
        isApi = true;

  @override
  State<AAutocompleteField<T>> createState() => _AAutocompleteFieldState<T>();
}

class _AAutocompleteFieldState<T> extends State<AAutocompleteField<T>> with FieldControllerCreatorMixin {
  late final AutocompleteController<T> fieldController;
  late final _localDecoration = widget.decoration ?? const InputDecoration();

  var key = GlobalKey();

  double _width = 0;

  @override
  void initState() {
    fieldController = createFieldController(context, widget.fieldController, widget.name, _buildController, widget.onControllerCreated);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      final size = getRenderBoxSize(key.currentContext!);
      setState(() {
        _width = size.width;
      });
    });
    super.initState();
  }

  @override
  void dispose() {
    fieldController.disposeByWidget();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final listChild = ValueListenableBuilder(
      valueListenable: fieldController.selectedIndex,
      builder: (_, selectedIndex, ___) {
        return Material(
          elevation: 5,
          child: _buildListView(selectedIndex),
        );
      },
    );

    return AConsumer(
      notifier: fieldController,
      builder: (context, fieldController, _) {
        final enabled = fieldController.enabled ?? true;
        final suffixIcon = _buildSuffix();

        return MenuAnchor(
          menuChildren: [
            SizedBox(
              width: max(_width, _kMinWidth),
              height: fieldController.createdBySpreadColumn ? 250 : 350,
              child: listChild,
            ),
          ],
          crossAxisUnconstrained: false,
          controller: fieldController.menuController,
          child: TextFormField(
            key: key,
            decoration: _localDecoration.copyWith(
              floatingLabelBehavior: FloatingLabelBehavior.always,
              errorText: fieldController.errorText,
              errorMaxLines: 2,
              labelText: _localDecoration.labelText ?? fieldController.labelText,
              hintText: _localDecoration.hintText ?? fieldController.hintText,
              helperText: _localDecoration.helperText ?? fieldController.helperText,
              suffixIcon: suffixIcon,
            ),
            mouseCursor: enabled ? SystemMouseCursors.click : SystemMouseCursors.basic,
            onTap: fieldController.toggleMenu,
            textAlignVertical: TextAlignVertical.center,
            enabled: enabled,
            readOnly: true,
            controller: fieldController.comboEditingController,
            focusNode: fieldController.focusNode,
          ),
        );
      },
    );
  }

  Widget _buildListView(int selectedIndex) {
    return ValueListenableBuilder(
      valueListenable: fieldController.loading,
      builder: (ctx, loading, __) {
        return _buildListItens(selectedIndex, loading);
      },
    );
  }

  Widget _buildListItens(int selectedIndex, bool loading) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: TextField(
            controller: fieldController.searchEditingController,
            focusNode: fieldController.searchFocusNode,
            onChanged: fieldController.onSearchTextChange,
            decoration: const InputDecoration(
              hintText: 'Pesquisar',
              prefixIcon: Icon(Icons.search),
            ),
          ),
        ),
        if (loading) const Center(child: CircularProgressIndicator()),
        if (!loading)
          fieldController.filteredOptions.isEmpty
              ? const Center(child: Text('Nenhum item encontrado'))
              : Expanded(
                  child: ListView.separated(
                    controller: fieldController.itemsScrollController,
                    itemCount: fieldController.filteredOptions.length,
                    separatorBuilder: (_, __) => const ADivider.lineOnly(),
                    itemBuilder: (ctx, index) => _buildItem(
                      ctx,
                      index,
                      selectedIndex,
                    ),
                  ),
                ),
      ],
    );
  }

  Widget _buildItem(BuildContext ctx, int index, int selectedIndex) {
    final item = fieldController.filteredOptions[index];
    final style = index == selectedIndex ? const TextStyle(fontWeight: FontWeight.bold, fontSize: 16) : null;
    return InkWell(
      canRequestFocus: false,
      onTapDown: (_) {
        fieldController.onListItemTap(item);
      },
      child: Container(
        color: index == selectedIndex ? _selectedBackgroundColor : null,
        child: Padding(
          padding: EdgeInsets.all(fieldController.createdBySpreadColumn ? 4.0 : 6.0),
          child: widget.listItemBuilder?.call(item) ?? Text(item.toString(), style: style),
        ),
      ),
    );
  }

  Widget? _buildSuffix() {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        if (fieldController.value != null && !fieldController.createdBySpreadColumn)
          IconButton(
            onPressed: () {
              fieldController.value = null;
              fieldController.focusNode.requestFocus();
            },
            focusNode: fieldController.suffixIconFocusNode,
            icon: const Icon(
              Icons.clear,
              size: 25,
              color: Colors.redAccent,
            ),
          ),
        ValueListenableBuilder(
          valueListenable: fieldController.loading,
          builder: (_, loading, __) {
            return loading
                ? const SizedBox(
                    width: 20,
                    height: 20,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                    ),
                  )
                : IconButton(
                    icon: const Icon(Icons.arrow_drop_down),
                    onPressed: fieldController.toggleMenu,
                    focusNode: fieldController.suffixIconFocusNode,
                  );
          },
        ),
      ],
    );
  }

  AutocompleteController<T> _buildController() {
    final AutocompleteController<T> controller;
    if (widget.options != null) {
      controller = AutocompleteController(
        widget.name!,
        repository: AllInMemoryAutocompleteRepository<T>(widget.options!),
        labelText: widget.labelText,
        hintText: widget.hintText,
        helperText: widget.helperText,
        enabled: widget.enabled,
        validators: widget.validators,
        req: widget.req ?? false,
        showAllOptions: widget.showAllOptions ?? false,
        autoFocus: widget.autoFocus ?? false,
        onSelectedValue: widget.onSelectedValue,
      );
    } else if (widget.isApi) {
      final repository = RemoteAPIAutocompleteRepository<T>(
        http: coreHttpProvider,
        autocompleteMetadataName: widget.autocompleteMetadataName ?? widget.name!,
        defaultWhereBuilder: widget.defaultWhereBuilder,
      );

      controller = AutocompleteController(
        widget.name!,
        repository: repository,
        labelText: widget.labelText,
        hintText: widget.hintText,
        helperText: widget.helperText,
        enabled: widget.enabled,
        validators: widget.validators,
        req: widget.req ?? false,
        autoFocus: widget.autoFocus ?? false,
        onSelectedValue: widget.onSelectedValue,
      );
    } else {
      throw Exception('ASelectField: options or fieldController must be provided');
    }

    return controller;
  }

  Size getRenderBoxSize(BuildContext context) {
    final box = context.findRenderObject() as RenderBox;
    return box.size;
  }
}
