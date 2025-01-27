import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrud extends StatefulWidget {
  final String taskName;
  final CrudDescr descr;
  final List<ASpreadColumn> columns;

  final List<Widget>? customFilters;

  final CrudController? controller;

  // ---- Para edição de registro ----
  // ---- Informar ou o OnEdit ou o FormBody ----
  final void Function(int? id)? onEdit;
  final Widget? formBody;

  const ACrud.name({
    required this.taskName,
    required this.descr,
    required this.columns,
    this.customFilters,
    this.controller,
    this.onEdit,
    this.formBody,
    super.key,
  });

  ACrud.controller({
    required CrudController this.controller,
    required this.descr,
    required this.columns,
    this.customFilters,
    this.onEdit,
    this.formBody,
    super.key,
  }) : taskName = controller.taskName;

  @override
  State<ACrud> createState() => _ACrudState();
}

class _ACrudState extends State<ACrud> {
  late final _controllerCreatedForThis = widget.controller != null;
  late final _controller = widget.controller ?? CrudController(taskName: widget.taskName);

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
        return SingleChildScrollView(
          child: AContainer(
            header: ACrudHeader.text(
              widget.descr.plural,
              onAddTap: _isEditable ? _onEdit : null,
            ),
            child: ASpacingColumn(
              spacing: 16,
              children: [
                AForm(
                  _controller.formFiltersController,
                  child: ACrudPanelFilters(
                    _controller,
                    customFilters: widget.customFilters,
                  ),
                ),
                ACrudSpreadDataCard(
                  crudController: _controller,
                  onEdit: _isEditable ? _onEdit : null,
                  columns: widget.columns,
                ),
              ],
            ),
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
      final saved = await ASideDialog.showBottom(
        builder: (context) => AEditCrud(
          descr: widget.descr,
          id: id,
          formBody: widget.formBody!,
        ),
        barrierDismissible: false,
      );
      if (saved != null) {
        _controller.doRefresh();
      }
    }
  }
}
