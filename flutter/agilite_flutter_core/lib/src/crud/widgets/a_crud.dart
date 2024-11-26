import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrud extends StatefulWidget {
  final CrudDescr descr;
  final List<ASpreadColumn> columns;

  final List<Widget>? customFilters;

  final CrudController? controller;
  final void Function(int? id)? onEdit;
  const ACrud({
    required this.descr,
    required this.columns,
    this.customFilters,
    this.controller,
    this.onEdit,
    super.key,
  });

  @override
  State<ACrud> createState() => _ACrudState();
}

class _ACrudState extends State<ACrud> {
  late final _controllerCreatedForThis = widget.controller != null;
  late final _controller = widget.controller ?? CrudController();

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
              onAddTap: widget.onEdit == null
                  ? null
                  : () {
                      widget.onEdit!(null);
                    },
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
                  onEdit: widget.onEdit,
                  columns: widget.columns,
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
