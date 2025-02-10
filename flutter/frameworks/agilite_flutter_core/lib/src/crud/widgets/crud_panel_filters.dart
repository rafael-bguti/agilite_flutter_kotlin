import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudPanelFilters extends StatelessWidget {
  final CrudController crudController;

  final List<Widget>? customFilters;
  final Widget? moreFiltersWidget;

  const CrudPanelFilters(
    this.crudController, {
    this.customFilters,
    this.moreFiltersWidget,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AGrid(
      [
        AGridRow(areas: "3, 9", children: [
          const ATextField.string(
            CrudController.searchName,
            hintText: 'Pesquisar',
            decoration: InputDecoration(
              prefixIcon: Icon(Icons.search),
            ),
          ),
          ASpacingRow(
            children: [
              ...(customFilters ?? []),
              if (moreFiltersWidget != null)
                OutlinedButton.icon(
                  style: buildOutlinedButtonStyle(primaryColor),
                  onPressed: () {
                    ASideDialog.showRight(
                      builder: (ctx) => _MoreFiltersPanel(
                        crudController: crudController,
                        moreFiltersWidget: moreFiltersWidget!,
                      ),
                    );
                  },
                  label: const Text("mais filtros"),
                  icon: const Icon(Icons.filter_alt_outlined),
                ),
              const Spacer(),
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
                      crudController.onClearFiltersClicked();
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
      crossAxisAlignment: WrapCrossAlignment.center,
    );
  }
}

class _MoreFiltersPanel extends StatelessWidget {
  final CrudController crudController;
  final Widget moreFiltersWidget;
  const _MoreFiltersPanel({
    required this.crudController,
    required this.moreFiltersWidget,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      child: SizedBox(
        height: MediaQuery.of(context).size.height,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const ADialogHeader(headerText: "Mais filtros"),
            const SizedBox(height: 16),
            Expanded(
              child: SingleChildScrollView(
                child: AForm(
                  crudController.moreFiltersFormController,
                  child: moreFiltersWidget,
                ),
              ),
            ),
            Container(
              height: 65,
              color: backgroundColor,
              child: Center(
                child: FilledButton.icon(
                  style: successButtonStyle,
                  onPressed: () {
                    Navigator.of(context).pop();
                    crudController.onBtnRefreshClick();
                  },
                  label: const Text("Fitlrar"),
                  icon: const Icon(Icons.check),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
