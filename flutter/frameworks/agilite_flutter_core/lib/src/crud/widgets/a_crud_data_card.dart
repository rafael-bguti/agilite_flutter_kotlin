import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrudSpreadDataCard extends StatelessWidget {
  final CrudController crudController;
  final List<ASpreadColumn> columns;
  final double height;
  final void Function(int id)? onEdit;

  const ACrudSpreadDataCard({
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
                ACrudDataGroups(crudController),
                AForm(
                  crudController.dataFormController,
                  child: Expanded(child: _buildSpread()),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 12),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8.0),
                        child: APaginator(
                          currentPage: crudController.state.currentPage,
                          pageSize: crudController.state.pageSize,
                          totalRecords: crudController.state.totalRecords,
                          onPageSizeChange: (pgSize) {
                            crudController.formFiltersController.setCustomValue(CrudController.pageSizeName, pgSize);
                            crudController.doRefresh();
                          },
                          onPageChange: (delta) {
                            final newPage = crudController.state.currentPage + delta;
                            crudController.formFiltersController.setCustomValue(CrudController.currentPageName, newPage);
                            crudController.doRefresh();
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

  Widget _buildSpread() {
    return ASpread(
      name: CrudController.spreadDataName,
      columns: columns,
      onRowTap: onEdit == null
          ? null
          : (rowIndex) {
              onEdit!(crudController.state.data[rowIndex]['id']);
            },
      readOnly: true,
      selectPanelWidget: TextButton.icon(
        onPressed: crudController.deleteSelecteds,
        style: TextButton.styleFrom(foregroundColor: Colors.red),
        icon: const Icon(Icons.delete_outline_outlined),
        label: const Text("excluir selecionados"),
      ),
    );
  }
}
