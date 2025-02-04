import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudVendas extends StatelessWidget {
  const CrudVendas({super.key});

  @override
  Widget build(BuildContext context) {
    return ACrud.name(
      taskName: "vendas",
      descr: const CrudDescr('Venda'),
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
      formBody: ASpacingColumn(
        children: const [
          ATextField.string("nome", labelText: "Nome", req: true),
          ATextField.string("descricao", labelText: "Descrição", req: false),
        ],
      ),
    );
  }
}
