import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'crud_data_groups.dart';

class CrudDataTableCard extends StatelessWidget {
  final CrudController crudController;
  final List<ASpreadColumn> columns;
  final double height;
  final void Function(int rowIndex)? onEdit;

  const CrudDataTableCard({
    required this.crudController,
    required this.columns,
    required this.onEdit,
    this.height = 550,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return ACard(
      padding: EdgeInsets.zero,
      child: SizedBox(
        height: height,
        child: AConsumer(
          notifier: crudController.$loading,
          builder: (_, __, child) {
            if (crudController.$loading.value) {
              return child!;
            }
            return Column(
              children: [
                CrudDataGroups(crudController),
                Expanded(
                  child: ASpread(
                    name: CrudController.spreadDataName,
                    columns: columns,
                    onRowTap: onEdit == null
                        ? null
                        : (rowIndex) {
                            onEdit!(crudController.state.data[rowIndex]['id'] as int);
                          },
                    readOnly: true,
                    controller: crudController.spreadController,
                    value: crudController.state.data,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8.0),
                        child: FilledButton.tonalIcon(
                          style: errorButtonStyle,
                          label: Text('Excluir selecionados'),
                          icon: const Icon(Icons.delete_outline_outlined),
                          onPressed: crudController.spreadController.selectedRows.isEmpty ? null : crudController.onDeleteClicked,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8.0),
                        child: APaginator(
                          currentPage: crudController.state.currentPage,
                          pageSize: crudController.state.currentPageSize,
                          infinite: true,
                          onPageSizeChange: (pgSize) {
                            crudController.onPageSizeChange(pgSize);
                          },
                          onPageChange: (delta) {
                            crudController.onPageNavigateClicked(delta);
                          },
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            );
          },
          child: Center(
            child: ASpacingColumn(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.center,
              children: const [
                CircularProgressIndicator(),
                Text('Carregando...'),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
