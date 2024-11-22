import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrudDataCard extends StatelessWidget {
  final ACrudController crudController;
  final double height;
  final void Function(int id) onEdit;

  const ACrudDataCard({
    required this.crudController,
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
                Expanded(child: _buildSpread()),
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
                            crudController.formFiltersController.setCustomValue(ACrudController.pageSizeName, pgSize);
                            crudController.doRefresh();
                          },
                          onPageChange: (delta) {
                            final newPage = crudController.state.currentPage + delta;
                            crudController.formFiltersController.setCustomValue(ACrudController.currentPageName, newPage);
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
    return ASpread.controller(
      crudController.spreadDataController,
      onRowTap: (rowIndex) {
        onEdit(crudController.state.data[rowIndex]['id']);
      },
      selectPanelWidget: TextButton.icon(
        onPressed: crudController.deleteSelecteds,
        style: TextButton.styleFrom(foregroundColor: Colors.red),
        icon: const Icon(Icons.delete_outline_outlined),
        label: const Text("excluir selecionados"),
      ),
    );
  }
}
