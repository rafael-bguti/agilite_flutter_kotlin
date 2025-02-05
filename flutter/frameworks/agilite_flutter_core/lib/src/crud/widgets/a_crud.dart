import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import './crud_data_table_card.dart';
import 'crud_header.dart';
import 'crud_panel_filters.dart';

class ACrud extends StatefulWidget {
  final String taskName;

  final CrudDescr descr;
  final List<ASpreadColumn> columns;

  final CrudController? controller;
  final String? metadataToLoad;

  // ---- More filters ----
  final List<Widget>? customFilters;
  final Widget? moreFiltersWidget;

  // ---- Para edição de registro ----
  // ---- Informar ou o OnEdit ou o FormBody ----
  final void Function(int? id)? onEdit;
  final Widget? formBody;

  const ACrud.name({
    required this.taskName,
    required this.descr,
    required this.columns,
    this.metadataToLoad,
    this.customFilters,
    this.controller,
    this.onEdit,
    this.formBody,
    this.moreFiltersWidget,
    super.key,
  });

  ACrud.controller({
    required CrudController this.controller,
    required this.descr,
    required this.columns,
    this.customFilters,
    this.onEdit,
    this.formBody,
    this.moreFiltersWidget,
    super.key,
  })  : taskName = controller.taskName,
        metadataToLoad = null;

  @override
  State<ACrud> createState() => _ACrudState();
}

class _ACrudState extends State<ACrud> {
  late final _controllerCreatedForThis = widget.controller != null;
  late final _controller = widget.controller ??
      CrudController(
        taskName: widget.taskName,
        metadataToLoad: widget.metadataToLoad,
      );

  @override
  void dispose() {
    if (!_controllerCreatedForThis) {
      _controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AView(
      controller: _controller,
      builder: (context, state) {
        return ATaskContainer(
          header: CrudHeader.text(
            widget.descr.plural,
            onAddTap: _isEditable ? _onEdit : null,
          ),
          child: ASpacingColumn(
            spacing: 16,
            children: [
              AForm(
                _controller.filtersFormController,
                child: CrudPanelFilters(
                  _controller,
                  customFilters: widget.customFilters,
                  moreFiltersWidget: widget.moreFiltersWidget,
                ),
              ),
              CrudDataTableCard(
                crudController: _controller,
                onEdit: _isEditable ? _onEdit : null,
                columns: widget.columns,
              ),
            ],
          ),
        );
      },
    );
  }

  bool get _isEditable => widget.onEdit != null || widget.formBody != null;

  Future<void> _onEdit([int? id]) async {
    if (widget.onEdit != null) {
      widget.onEdit!(id);
    } else {
      final body = await _loadFormBody();
      final saved = await ASideDialog.showBottom(
        builder: (context) => AEditCrud(
          taskName: widget.taskName,
          descr: widget.descr,
          id: id,
          formBody: body,
        ),
        barrierDismissible: false,
      );
      if (saved != null) {
        _controller.onBtnRefreshClick();
      }
    }
  }

  FutureOr<Widget> _loadFormBody() async {
    if (activeProfile.type == CoreProfileType.dev) {
      final formContent = await RemoteSduiContentProvider(
        url: '/sdui/crudForm/${widget.taskName}',
      ).getContent();

      final sduiContext = SduiContext();
      return SduiRender.renderFromJson(
        context,
        sduiContext,
        formContent!,
      );
    }
    return widget.formBody!;
  }
}
