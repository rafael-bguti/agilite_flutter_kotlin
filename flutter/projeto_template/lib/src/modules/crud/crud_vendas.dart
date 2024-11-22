import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:projeto_estudo/src/modules/crud/edit_crud_vendas.dart';

class CrudVendas extends StatefulWidget {
  const CrudVendas({super.key});

  @override
  State<CrudVendas> createState() => _CrudVendasState();
}

class _CrudVendasState extends State<CrudVendas> {
  late final CrudController crudController;

  @override
  void initState() {
    super.initState();
    crudController = CrudController(
      dataColumns: [
        AColumnString("name", "Name"),
        AColumnString("email", "Email"),
      ],
    );
  }

  @override
  void dispose() {
    crudController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AView(
      controller: crudController,
      builder: (context, state) {
        return SingleChildScrollView(
          child: AContainer(
            header: ACrudHeader.text('Vendas', onAddTap: () {
              _showForm(null);
            }),
            child: ASpacingColumn(
              spacing: 16,
              children: [
                AForm(
                  crudController.formFiltersController,
                  child: ACrudPanelFilters(
                    crudController,
                    customFilters: [
                      OutlinedButton.icon(
                        style: buildOutlinedButtonStyle(primaryColor),
                        onPressed: () {},
                        label: const Text("emissão"),
                        icon: const Icon(Icons.calendar_today),
                      ),
                    ],
                  ),
                ),
                ACrudDataCard(
                  crudController: crudController,
                  onEdit: _showForm,
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  void _showForm(int? id) async {
    final form = CrudEditVendas(id: id);

    final saved = await ASideDialog.showBottom(
      builder: (context) => form,
      barrierDismissible: false,
    );

    if (saved != null) {
      crudController.doRefresh();
    }
  }
}
