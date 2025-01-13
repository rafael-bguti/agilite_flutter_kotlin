import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const _moreActionWidth = 35.0;

class ASpread extends StatefulWidget {
  final SpreadController? controller;
  final Widget? selectPanelWidget;

  //Builders
  final Widget Function(BuildContext context, SpreadController controller, int rowIndex, Widget rowContent)? rowBuilder;
  final Widget Function(BuildContext context, SpreadController controller, int rowIndex, Widget row)? rowWrapperBuilder;
  final List<SpreadMoreOptionAction>? Function(int row)? moreOptionActionsBuilder;

  //Mais detalhes
  final SpreadMoreDetail? moreDetail;

  final List<Map<String, dynamic>>? value;

  final String? name;
  final List<ASpreadColumn<dynamic>>? columns;
  final bool? disableScroll;
  final bool? disableVerticalScroll;
  final bool? readOnly;
  final String? selectColumnName;
  final String labelButtonAddNew;

  //Actions
  final void Function(int rowIndex)? onRowTap;
  final FutureOr<void> Function(Map<String, dynamic> row)? onAddNewRow;
  final FutureOr<bool> Function()? canAddNewRow;

  final void Function(SpreadController controller, int row, String columnName)? onCellStopEdit;

  final void Function(SpreadController controller)? onControllerCreated;

  final String? labelTextToValidationMessage;

  final List<Map<String, dynamic?>>? initialData;

  const ASpread.controller(
    this.controller, {
    this.rowBuilder,
    this.rowWrapperBuilder,
    this.selectPanelWidget,
    this.onRowTap,
    this.moreOptionActionsBuilder,
    this.moreDetail,
    this.value,
    this.labelButtonAddNew = 'Adicionar nova linha',
    super.key,
  })  : columns = null,
        name = null,
        disableScroll = null,
        disableVerticalScroll = null,
        selectColumnName = null,
        onAddNewRow = null,
        canAddNewRow = null,
        readOnly = null,
        onCellStopEdit = null,
        onControllerCreated = null,
        labelTextToValidationMessage = null,
        initialData = null;

  const ASpread.columns(
    String this.name,
    this.columns, {
    this.rowBuilder,
    this.selectColumnName,
    this.selectPanelWidget,
    this.onRowTap,
    this.disableScroll = false,
    this.disableVerticalScroll = false,
    this.moreOptionActionsBuilder,
    this.moreDetail,
    this.onAddNewRow,
    this.canAddNewRow,
    this.readOnly,
    this.onCellStopEdit,
    this.value,
    this.onControllerCreated,
    this.labelButtonAddNew = 'Adicionar nova linha',
    this.labelTextToValidationMessage,
    this.rowWrapperBuilder,
    this.initialData,
    super.key,
  }) : controller = null;

  @override
  State<ASpread> createState() => _ASpreadState();
}

class _ASpreadState extends State<ASpread> with FieldControllerRegisterMixin {
  late final SpreadController spreadController;
  late final List<SpreadMoreOptionAction>? Function(int row)? _localMoreOptionActionsBuilder =
      widget.moreOptionActionsBuilder ?? _buildDefaultMoreOptionActionsBuilder(spreadController);

  @override
  initState() {
    super.initState();
    spreadController = registerFormField(
      context,
      widget.controller,
      widget.name,
      _buildController,
      widget.onControllerCreated,
    );
    spreadController.registerOnRowTap(widget.onRowTap);
    spreadController.registerMoreDetail(widget.moreDetail);

    for (var column in spreadController.columns) {
      column.spreadController = spreadController;
    }

    if (widget.initialData != null) {
      spreadController.fillFromList(widget.initialData!);
    }
  }

  @override
  void didUpdateWidget(ASpread oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (oldWidget.value != widget.value) {
      spreadController.value = SpreadModel.value(widget.value);
    }
  }

  @override
  Widget build(BuildContext context) {
    return AConsumer(
      notifier: spreadController,
      builder: (context, controller, _) {
        final table = _buildTable(controller);
        if (widget.selectPanelWidget == null) return table;

        return Stack(
          fit: StackFit.expand,
          children: [
            table,
            _buildSelectedPanel(context, controller, widget.selectPanelWidget!),
          ],
        );
      },
    );
  }

  Widget _buildTable(SpreadController controller) {
    final showHorizontalScroll = controller.isAllColumnsFixedWidth;

    if (controller.disableScroll || controller.disableVerticalScroll) {
      final column = Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: showHorizontalScroll ? CrossAxisAlignment.start : CrossAxisAlignment.stretch,
        children: [
          _buildHeader(controller),
          _buildBody(controller),
        ],
      );

      if (controller.disableScroll) return column;
      return AScrollView(addHorizontalScroll: true, body: column);
    } else {
      return AScrollView(
        scrollController: controller.scrollController,
        addHorizontalScroll: showHorizontalScroll,
        header: _buildHeader(controller),
        body: _buildBody(controller),
      );
    }
  }

  Widget _buildHeader(SpreadController controller) {
    return Container(
      height: controller.rowHeight,
      decoration: tableHeaderRowDecoration,
      child: Row(
        children: [
          SizedBox(width: _localMoreOptionActionsBuilder != null ? _moreActionWidth : 0),
          for (var column in controller.columns) column.buildHeader(context),
        ],
      ),
    );
  }

  Widget _buildBody(SpreadController controller) {
    return Focus(
      focusNode: controller.focusNode,
      child: ValueListenableBuilder(
        valueListenable: controller.loading,
        builder: (context, loading, child) {
          return loading ? const Center(child: CircularProgressIndicator()) : child!;
        },
        child: _buildRows(controller),
      ),
    );
  }

  Widget _buildRows(SpreadController controller) {
    final showFooter = controller.columns.any((element) => element.showFooter());
    if (controller.value.isEmpty) {
      return ASpacingColumn(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          if (!controller.readOnly) _buttonAddNewLine(),
        ],
      );
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        for (var i = 0; i < controller.value.length; i++) _buildRowWithWrapper(controller, i),
        if (showFooter) _buildFooter(controller),
        if (!controller.readOnly) _buttonAddNewLine(),
      ],
    );
  }

  Widget _buildRowWithWrapper(SpreadController controller, int index) {
    final row = _buildRow(controller, index);
    if (widget.rowWrapperBuilder != null) {
      return widget.rowWrapperBuilder!(context, controller, index, row);
    } else {
      return row;
    }
  }

  Widget _buildFooter(SpreadController controller) {
    return Container(
      height: controller.rowHeight,
      decoration: tableHeaderRowDecoration,
      child: Row(
        children: [
          SizedBox(width: _localMoreOptionActionsBuilder != null ? _moreActionWidth : 0),
          for (var column in controller.columns) column.buildFooter(context),
        ],
      ),
    );
  }

  Widget _buttonAddNewLine() {
    return Padding(
      padding: const EdgeInsets.all(4.0),
      child: TextButton.icon(
        onPressed: spreadController.addRow,
        icon: const Icon(Icons.add),
        label: Text(widget.labelButtonAddNew),
      ),
    );
  }

  Widget _buildRow(SpreadController controller, int index) {
    final rowContent = _buildRowContent(controller, index);

    if (widget.rowBuilder != null) {
      return widget.rowBuilder!(context, controller, index, rowContent);
    } else {
      bool isSelected = controller.isRowSelected(index);
      final wrapper = Container(
        key: ValueKey('row$index'),
        height: controller.rowHeight,
        decoration: tableRowDecoration.copyWith(color: isSelected ? primaryColor.withOpacity(0.2) : null),
        child: rowContent,
      );

      if (controller.readOnly && widget.onRowTap != null) {
        return InkWell(
          onTap: () => widget.onRowTap?.call(index),
          child: wrapper,
        );
      } else {
        return wrapper;
      }
    }
  }

  Widget _buildRowContent(SpreadController controller, int index) {
    final row = Row(
      children: [
        _buildMoreOptionsMenu(controller, index),
        for (var column in controller.columns) column.buildCell(context, index),
      ],
    );

    if (controller.hasSelectColumn) {
      bool isSelected = controller.isRowSelected(index);
      return Stack(
        children: [
          row,
          AnimatedContainer(
            duration: kThemeAnimationDuration,
            color: Theme.of(context).primaryColor,
            width: isSelected ? 5 : 0,
            height: isSelected ? controller.rowHeight : 0,
          )
        ],
      );
    } else {
      return row;
    }
  }

  Widget _buildMoreOptionsMenu(SpreadController controller, int index) {
    if (_localMoreOptionActionsBuilder == null || _localMoreOptionActionsBuilder.call(index) == null) {
      return const SizedBox.shrink();
    }
    return PopupMenuButton(
      tooltip: '',
      elevation: 15,
      itemBuilder: (context) {
        return _localMoreOptionActionsBuilder
            .call(index)!
            .map(
              (e) => PopupMenuItem(
                height: e.height,
                onTap: e.onTap != null ? () => e.onTap!(context, index, controller) : null,
                enabled: e.onTap != null,
                child: e.child,
              ),
            )
            .toList();
      },
      child: SizedBox(
        width: _moreActionWidth,
        height: controller.rowHeight,
        child: const Icon(Icons.more_vert),
      ),
    );
  }

  SpreadController _buildController() {
    final controller = SpreadController(
      widget.name!,
      columns: widget.columns!,
      disableScroll: widget.disableScroll ?? false,
      disableVerticalScroll: widget.disableVerticalScroll ?? false,
      selectColumnName: widget.selectColumnName,
      readOnly: widget.readOnly == null ? widget.onRowTap != null : widget.readOnly!,
      onAddNewRow: widget.onAddNewRow,
      canAddNewRow: widget.canAddNewRow,
      onCellStopEdit: widget.onCellStopEdit,
      labelTextToValidationMessage: widget.labelTextToValidationMessage,
    );

    return controller;
  }

  Widget _buildSelectedPanel(BuildContext context, SpreadController controller, Widget selectPanelWidget) {
    final theme = Theme.of(context);

    return AnimatedPositioned(
      key: ValueKey('a_crud_list-selected_panel-${controller.name}'),
      duration: kThemeAnimationDuration,
      curve: Curves.fastOutSlowIn,
      bottom: controller.selectedRowCount > 0 ? 16 : -90,
      left: 0,
      right: 0,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        child: ACard(
          padding: EdgeInsets.zero,
          child: Container(
            width: double.infinity,
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 16),
            color: backgroundColor.withOpacity(0.7),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ASpacingRow(
                  children: [
                    Text(
                      _getSelectedRowsText(controller.selectedRowCount),
                      style: theme.textTheme.bodyLarge,
                    ),
                    TextButton(
                      onPressed: () => controller.unselectAllRows(),
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.orange,
                      ),
                      child: Text(_getUnselectText(controller.selectedRowCount)),
                    )
                  ],
                ),
                Row(
                  children: [
                    const VerticalDivider(indent: 10, endIndent: 10),
                    selectPanelWidget,
                  ],
                )
              ],
            ),
          ),
        ),
      ),
    );
  }

  String _getSelectedRowsText(int count) {
    if (count == 1) return '1 registro selecionado';
    return '$count registros selecionados';
  }

  String _getUnselectText(int count) {
    if (count == 1) return 'desselecionar';
    return 'desselecionar todos';
  }

  List<SpreadMoreOptionAction>? Function(int row)? _buildDefaultMoreOptionActionsBuilder(SpreadController controller) {
    if (controller.readOnly) return null;

    final options = [
      spreadMoreActionAddNewRow,
      spreadMoreActionRemoveRow,
    ];

    if (widget.moreDetail != null) {
      options.insert(
        0,
        SpreadMoreOptionAction(
          child: ASpacingRow(
            children: [
              const Icon(Icons.edit),
              Expanded(child: Text(widget.moreDetail!.moreOptionActionText)),
              const Text('(Ctrl+D)', style: TextStyle(fontSize: 10)),
            ],
          ),
          onTap: (ctx, row, controller) => controller.onMoreDetailTap(row),
        ),
      );

      options.insert(1, SpreadMoreOptionAction.divider());
    }

    return (row) => options;
  }
}

final spreadMoreActionAddNewRow = SpreadMoreOptionAction(
  child: ASpacingRow(
    children: const [
      Icon(Icons.new_label_outlined),
      Expanded(child: Text('nova linha acima')),
      Text('(Ctrl+N)', style: TextStyle(fontSize: 10)),
    ],
  ),
  onTap: (ctx, row, controller) => controller.insertRow(row),
);

final spreadMoreActionRemoveRow = SpreadMoreOptionAction(
  child: ASpacingRow(children: const [
    Icon(
      Icons.delete,
      color: Colors.red,
    ),
    Expanded(
      child: Text(
        'excluir linha',
        style: TextStyle(
          color: Colors.red,
        ),
      ),
    ),
    Text('(Ctrl+E)', style: TextStyle(color: Colors.red, fontSize: 10)),
  ]),
  onTap: (ctx, row, controller) => controller.doDeleteFromKeyPressed(row),
);

class SpreadMoreOptionAction {
  final double height;
  final Widget child;
  final void Function(BuildContext context, int rowIndex, SpreadController controller)? onTap;

  SpreadMoreOptionAction({
    required this.child,
    required this.onTap,
    this.height = 35,
  });

  SpreadMoreOptionAction.divider()
      : child = const ADivider.lineOnly(),
        onTap = null,
        height = 8;
}

class SpreadMoreDetail {
  final String moreOptionActionText;

  Widget Function(BuildContext context, int rowIndex, SpreadController controller)? moreDetailBodyBuilder;
  Widget Function(BuildContext context, int rowIndex, SpreadController controller)? otherWidgetsBuilder;
  void Function(int rowIndex, SpreadController controller)? onShowValue;
  final List<String> gridAreas;

  SpreadMoreDetail({
    this.moreOptionActionText = 'editar mais detalhes',
    this.moreDetailBodyBuilder,
    this.otherWidgetsBuilder,
    this.gridAreas = const ['12'],
  });

  Widget buildMoreDetailBody(BuildContext context, int rowIndex, SpreadController controller) {
    return moreDetailBodyBuilder?.call(context, rowIndex, controller) ?? _defaultMoreDetailBodyBuilder(context, rowIndex, controller);
  }

  void showValue(int rowIndex, SpreadController spreadController) {
    spreadController.moreDetailFormController.clear();
    spreadController.moreDetailFormController.value = spreadController.value[rowIndex].toMap();
    onShowValue?.call(rowIndex, spreadController);
  }

  void onDialogClose(int rowIndex, SpreadController controller) {
    final rowMap = controller.moreDetailFormController.buidlJson();

    controller.value[rowIndex] = rowMap;
    controller.refreshUi();
  }

  void focusFirstField(SpreadController controller) {
    controller.moreDetailFormController.getController(controller.columns.first.name)?.requestFocus();
  }

  Widget _defaultMoreDetailBodyBuilder(BuildContext context, int rowIndex, SpreadController controller) {
    final result = Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          const ADialogHeader(
            headerText: 'Mais detalhes',
          ),
          const ADivider.lineOnly(),
          Expanded(
            child: SingleChildScrollView(
              child: AForm(
                controller.moreDetailFormController,
                child: Focus(
                  onKey: (node, event) {
                    if (event.logicalKey == LogicalKeyboardKey.enter) {
                      Navigator.of(globalNavigatorKey.currentContext!).pop();
                      return KeyEventResult.handled;
                    }
                    return KeyEventResult.ignored;
                  },
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      AGrid(
                        areas: gridAreas,
                        children: _getMoreDetailBodyChildren(context, rowIndex, controller),
                      ),
                      otherWidgetsBuilder?.call(context, rowIndex, controller) ?? const SizedBox.shrink()
                    ],
                  ),
                ),
              ),
            ),
          ),
          const ADivider.lineOnly(),
          Padding(
            padding: const EdgeInsets.only(right: 8.0, bottom: 8.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                FilledButton.icon(
                  style: FilledButton.styleFrom(
                    foregroundColor: onSuccessColor,
                    backgroundColor: successColor,
                  ),
                  onPressed: () {
                    Navigator.of(globalNavigatorKey.currentContext!).pop();
                  },
                  icon: const Icon(Icons.check),
                  label: const Tooltip(
                    message: 'Ok (Enter)',
                    child: Text("Ok"),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
    return result;
  }

  List<Widget> _getMoreDetailBodyChildren(BuildContext context, int rowIndex, SpreadController controller) {
    List<Widget> result = [];
    bool autoFocus = true;
    for (var column in controller.columns) {
      final input = column.buildTextInputFromColumn(context, autoFocus: autoFocus);
      autoFocus = false;
      if (input != null) {
        result.add(input);
      }
    }
    return result;
  }
}
