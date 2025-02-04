import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'crud_data_groups.dart';

class CrudDataTableCard extends StatelessWidget {
  final CrudController crudController;
  final List<ADataTableColumn> columns;
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
                  child: ADataTable(
                    columns: columns,
                    rows: crudController.state.data,
                    onSelectedRowsChanged: (selectedRows) {
                      crudController.onSelectedRowsChanged(selectedRows);
                    },
                    onRowTap: onEdit == null
                        ? null
                        : (rowData) {
                            onEdit!(rowData['id'] as int);
                          },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8.0),
                        child: FilledButton.tonal(
                          style: errorButtonStyle,
                          child: Text('Excluir selecionados'),
                          onPressed: crudController.selectedRows.isEmpty ? null : crudController.onDeleteClicked,
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
