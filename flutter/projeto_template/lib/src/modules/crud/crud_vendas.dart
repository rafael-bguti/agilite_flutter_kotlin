import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudVendas extends StatefulWidget {
  const CrudVendas({super.key});

  @override
  State<CrudVendas> createState() => _CrudVendasState();
}

class _CrudVendasState extends State<CrudVendas> {
  late final CrudController crudController = CrudController();

  @override
  void dispose() {
    crudController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ACrud(
      controller: crudController,
      descr: _descr,
      customFilters: [
        OutlinedButton.icon(
          style: buildOutlinedButtonStyle(primaryColor),
          onPressed: () {},
          label: const Text("emissão"),
          icon: const Icon(Icons.calendar_today),
        ),
      ],
      columns: [
        AColumnString("name", "Name"),
        AColumnString("email", "Email"),
      ],
      onEdit: _showForm,
    );
  }

  void _showForm(int? id) async {
    final editCrud = AEditCrud(
      descr: _descr,
      id: id,
    );

    // final saved = await ASideDialog.showBottom(
    //   builder: (context) => editCrud,
    //   barrierDismissible: false,
    // );

    final saved = await ANavigator.pushWidget(
      editCrud,
    );

    if (saved != null) {
      crudController.doRefresh();
    }
  }

  CrudDescr get _descr => const CrudDescr('Venda');
}
