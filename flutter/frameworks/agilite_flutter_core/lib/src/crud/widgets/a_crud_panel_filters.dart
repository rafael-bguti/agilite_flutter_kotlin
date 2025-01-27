import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrudPanelFilters extends StatelessWidget {
  final CrudController crudController;

  final Widget Function(BuildContext context)? moreFiltersPanelBuilder;
  final List<Widget>? customFilters;
  const ACrudPanelFilters(
    this.crudController, {
    this.customFilters,
    this.moreFiltersPanelBuilder,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AGrid.rows(
      crossAxisAlignment: WrapCrossAlignment.center,
      rows: [
        AGridRow(areas: "3, 9", children: [
          const ATextField.string(
            CrudController.searchName,
            hintText: 'Pesquisar',
            decoration: InputDecoration(
              prefixIcon: Icon(Icons.search),
            ),
          ),
          Row(
            children: [
              ...(customFilters ?? []),
              const Spacer(),
              if (moreFiltersPanelBuilder != null)
                OutlinedButton.icon(
                  style: buildOutlinedButtonStyle(primaryColor),
                  onPressed: () {
                    ASideDialog.showRight(
                      builder: moreFiltersPanelBuilder!,
                    );
                  },
                  label: const Text("mais filtros"),
                  icon: const Icon(Icons.filter_alt_outlined),
                ),
              AConsumer(
                notifier: crudController.$loading,
                builder: (_, __, child) {
                  if (crudController.$loading.value || !crudController.hasFilters) {
                    return const SizedBox();
                  }
                  return child!;
                },
                child: Padding(
                  padding: const EdgeInsets.only(left: 8.0),
                  child: OutlinedButton.icon(
                    style: buildOutlinedButtonStyle(errorColor),
                    onPressed: () {
                      crudController.clearFilters();
                    },
                    label: const Text("limpar filtros"),
                    icon: const Icon(Icons.clear),
                  ),
                ),
              )
            ],
          ),
        ]),
      ],
    );
  }
}
